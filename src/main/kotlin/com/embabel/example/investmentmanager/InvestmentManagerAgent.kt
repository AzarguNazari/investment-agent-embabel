package com.embabel.example.investmentmanager

import com.embabel.agent.api.annotation.AchievesGoal
import com.embabel.agent.api.annotation.Action
import com.embabel.agent.api.annotation.Agent
import com.embabel.agent.api.common.OperationContext
import com.embabel.example.investmentmanager.dto.*
import com.embabel.example.investmentmanager.model.*
import com.embabel.example.investmentmanager.repository.*
import org.slf4j.LoggerFactory
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
        private val logger = LoggerFactory.getLogger(InvestmentManagerAgent::class.java)

        private val dummyOffers =
                listOf(
                        Offer(
                                "off1",
                                "Personalized Strategy",
                                "Get a free 1-on-1 advisor session",
                                "Free Session"
                        ),
                        Offer(
                                "off2",
                                "Tax-Loss Harvesting",
                                "Let AI optimize your tax liability",
                                "Save up to $2k"
                        ),
                        Offer(
                                "off3",
                                "Venture Access",
                                "Early access to Series B startup funding",
                                "Exclusive"
                        ),
                        Offer(
                                "off4",
                                "High-Yield Savings",
                                "Upgrade to Platinum for 5.5% APY",
                                "Interest Bonus"
                        )
                )

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

                logger.info(
                        "Retrieved ${transactions.transactions.size} transactions, ${journals.journals.size} journals, ${documents.documents.size} documents for account ${account.accountId}"
                )

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
                                        "Account ID: ${account.accountId}, Owner: ${account.owner.legalName}, Email: ${account.owner.email}, Bank: ${account.bankAccount.bankName}.\n" +
                                                "Current Holdings:\n$investmentSummary\n" +
                                                "Recent Transactions:\n$txSummary\n" +
                                                "Accounting Journals:\n$journalSummary\n" +
                                                "Available Documents:\n$docSummary\n" +
                                                "Answer the user's question: ${request.userQuery}. " +
                                                "Output your response in professionally formatted Markdown. " +
                                                "Use clear section headers, bullet points for lists, and **bold** for emphasis where appropriate. " +
                                                "Do not use HTML tags.",
                                        String::class.java
                                ) as
                                String

                return aiExplanation
        }

        @Action
        fun getRelevantOffers(account: CustomerAccount, context: OperationContext): OfferList {
                return OfferList(dummyOffers.take(2))
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
                                        "Generate a premium, full investment report for ${account.owner.legalName}.\n" +
                                                "Account Details: ID ${account.accountId}, Email ${account.owner.email}, Bank ${account.bankAccount.bankName}, IBAN ${account.bankAccount.iban}, Cash $${account.balances.settledCash}.\n" +
                                                "Current Holdings:\n$investmentSummary\n\n" +
                                                "Total Valuation: $${totalValuation + account.balances.settledCash}\n\n" +
                                                "Recent Transactions:\n$txSummary\n\n" +
                                                "Available Documents:\n$docSummary\n\n" +
                                                "Recommended Offers: ${offers.offers.joinToString(", ") { it.title }}\n\n" +
                                                "Output the report in premium, professionally formatted Markdown with clear section headers (using # and ##), bold text for emphasis, and organized lists. Do not use HTML.",
                                        String::class.java
                                ) as
                                String

                return report
        }
}
