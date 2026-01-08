package com.embabel.example.agent

import com.embabel.agent.api.annotation.AchievesGoal
import com.embabel.agent.api.annotation.Action
import com.embabel.agent.api.annotation.Agent
import com.embabel.agent.api.common.OperationContext
import com.embabel.example.Offers
import com.embabel.example.model.CustomerAccount
import com.embabel.example.model.CustomerId
import com.embabel.example.model.DocumentList
import com.embabel.example.model.InvestmentEnquiryRequest
import com.embabel.example.model.InvestmentList
import com.embabel.example.model.JournalList
import com.embabel.example.model.OfferList
import com.embabel.example.model.TransactionList
import com.embabel.example.repository.CustomerAccountRepository
import com.embabel.example.repository.DocumentRepository
import com.embabel.example.repository.JournalRepository
import com.embabel.example.repository.PortfolioRepository
import com.embabel.example.repository.PositionRepository
import com.embabel.example.repository.TransactionRepository
import org.springframework.stereotype.Service

@Agent(description = "Investment Manager Agent")
@Service
class InvestmentManagerAgent(
        private val accountRepository: CustomerAccountRepository,
        private val portfolioRepository: PortfolioRepository,
        private val positionRepository: PositionRepository,
        private val transactionRepository: TransactionRepository,
        private val journalRepository: JournalRepository,
        private val documentRepository: DocumentRepository
) {
        @Action
        fun getCustomerId(
                request: InvestmentEnquiryRequest,
                context: OperationContext
        ): CustomerId {
                return context.ai()
                        .withAutoLlm()
                        .createObject(
                                "Analyze this user query: \"${request.userQuery}\".\n" +
                                        "Identify if the user has provided a Customer Account ID.\n" +
                                        "Standard ID format: 'ACC-' followed by alphanumeric characters (e.g., 'ACC-7782XJ', 'ACC-1001-A', 'ACC-3001-B').\n" +
                                        "Return a CustomerId object with the extracted 'id'.\n" +
                                        "If NO ID is found or if it looks invalid, return the id as an empty string.",
                                CustomerId::class.java
                        ) as
                        CustomerId
        }

        @Action
        fun getAccountInfo(customerId: CustomerId, context: OperationContext): CustomerAccount {
                if (customerId.id.isEmpty()) {
                        throw IllegalArgumentException(
                                "Customer ID is required but was not provided."
                        )
                }
                return accountRepository.findById(customerId.id).orElseThrow {
                        IllegalArgumentException("Account not found for ID: ${customerId.id}")
                }
        }

        @Action
        fun getInvestments(customerId: CustomerId, context: OperationContext): InvestmentList {
                if (customerId.id.isEmpty()) {
                        return InvestmentList(emptyList())
                }
                val portfolios = portfolioRepository.findByLinkedAccountId(customerId.id)
                val positions =
                        portfolios.flatMap { positionRepository.findByPortfolioId(it.portfolioId) }
                return InvestmentList(positions)
        }

        @Action
        fun getTransactions(customerId: CustomerId, context: OperationContext): TransactionList {
                if (customerId.id.isEmpty()) {
                        return TransactionList(emptyList())
                }
                return TransactionList(transactionRepository.findByAccountId(customerId.id))
        }

        @Action
        fun getJournals(customerId: CustomerId, context: OperationContext): JournalList {
                if (customerId.id.isEmpty()) {
                        return JournalList(emptyList())
                }
                return JournalList(journalRepository.findByAccountId(customerId.id))
        }

        @Action
        fun getDocuments(customerId: CustomerId, context: OperationContext): DocumentList {
                if (customerId.id.isEmpty()) {
                        return DocumentList(emptyList())
                }
                return DocumentList(documentRepository.findByAccountId(customerId.id))
        }

        @Action
        @AchievesGoal(
                description =
                        "Answers any questions about the account, including holdings, transactions, journals, documents, or personal info like email and bank details."
        )
        fun explainInvestment(
                request: InvestmentEnquiryRequest,
                account: CustomerAccount,
                investments: InvestmentList,
                transactions: TransactionList,
                journals: JournalList,
                documents: DocumentList,
                context: OperationContext
        ): String {
                val investmentSummary =
                        investments.investments.joinToString("\n") {
                                "${it.assetDetails.ticker} (${it.assetDetails.instrumentType}): Qty: ${it.holdingData.quantity}, MarketVal: ${it.holdingData.marketValue}, P/L: ${it.performance.unrealizedGainLoss}"
                        }

                val txSummary =
                        transactions.transactions.take(5).joinToString("\n") {
                                "${it.date}: ${it.description} ${it.amount}"
                        }
                val journalSummary =
                        journals.journals.take(5).joinToString("\n") {
                                "${it.entryDate}: ${it.description} [${it.debitAccount} / ${it.creditAccount}] ${it.amount}"
                        }
                val docSummary =
                        documents.documents.joinToString("\n") {
                                "${it.issueDate}: ${it.title} (${it.type})"
                        }

                val aiExplanation =
                        context.ai()
                                .withAutoLlm()
                                .createObject(
                                        "You are a friendly and professional personal investment advisor. " +
                                                "Account Details: ID ${account.accountId}, Name: ${account.owner.legalName}, Email: ${account.owner.email}.\n" +
                                                "Context Data:\n" +
                                                "- Current Holdings:\n$investmentSummary\n" +
                                                "- Recent Transactions:\n$txSummary\n" +
                                                "- Accounting Journals:\n$journalSummary\n" +
                                                "- Available Documents:\n$docSummary\n\n" +
                                                "User Question: ${request.userQuery}\n\n" +
                                                "Response Guidelines:\n" +
                                                "1. Write in the style of a warm, professional email to your client.\n" +
                                                "2. Start with a friendly personalized greeting (e.g., 'Dear ${account.owner.legalName},').\n" +
                                                "3. Provide a clear and helpful answer to their specific question using the context data.\n" +
                                                "4. Use professionally formatted Markdown (headers, bold text, lists).\n" +
                                                "5. End with a supportive sign-off (e.g., 'Best regards, Your Investment Assistant').\n" +
                                                "6. Do not use HTML tags or emojis.\n" +
                                                "7. Keep the response as a clean, formal email text.",
                                        String::class.java
                                ) as
                                String

                return aiExplanation
        }

        @Action
        fun getRelevantOffers(account: CustomerAccount, context: OperationContext): OfferList {
                return OfferList(Offers.dummyOffers.take(2))
        }

        @Action
        @AchievesGoal(
                description =
                        "Generates a full account summary including balance, holdings, transactions, and offers. Use this for 'report', 'account status', 'full info' or 'portfolio summary'."
        )
        fun generateFullReport(
                account: CustomerAccount,
                investments: InvestmentList,
                transactions: TransactionList,
                journals: JournalList,
                documents: DocumentList,
                offers: OfferList,
                context: OperationContext
        ): String {
                val totalValuation = investments.investments.sumOf { it.holdingData.marketValue }
                val txSummary =
                        transactions.transactions.take(10).joinToString("\n") {
                                "${it.date}: ${it.description} ${it.amount}"
                        }
                val docSummary =
                        documents.documents.joinToString("\n") {
                                "${it.issueDate}: ${it.title} (${it.type})"
                        }
                val investmentSummary =
                        investments.investments.joinToString("\n") {
                                "${it.assetDetails.ticker}: Qty ${it.holdingData.quantity}, Value $${it.holdingData.marketValue}"
                        }

                val report =
                        context.ai()
                                .withAutoLlm()
                                .createObject(
                                        "Generate a premium, comprehensive portfolio review for ${account.owner.legalName}.\n" +
                                                "Account Details: ID ${account.accountId}, Email ${account.owner.email}, Bank ${account.bankAccount.bankName}, Cash $${account.balances.settledCash}.\n" +
                                                "Current Holdings:\n$investmentSummary\n\n" +
                                                "Financial Overview:\n" +
                                                "- Total Portfolio Value: $${totalValuation + account.balances.settledCash}\n" +
                                                "- Recent Transactions:\n$txSummary\n" +
                                                "- Available Documents:\n$docSummary\n\n" +
                                                "Offers For You: ${offers.offers.joinToString(", ") { it.title }}\n\n" +
                                                "Response Guidelines:\n" +
                                                "1. Format this as a formal, executive investment report delivered via email.\n" +
                                                "2. Start with a professional header and greeting.\n" +
                                                "3. Use premium, professionally formatted Markdown with clear section headers (# and ##).\n" +
                                                "4. Use tables or organized lists for clarity.\n" +
                                                "5. Include a closing summary and professional sign-off.\n" +
                                                "6. Maintain a supportive, sophisticated, and trustworthy tone throughout.\n" +
                                                "7. Do not use emojis or unnecessary decorative characters.",
                                        String::class.java
                                ) as
                                String

                return report
        }
}
