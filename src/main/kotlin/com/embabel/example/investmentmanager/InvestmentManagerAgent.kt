package com.embabel.example.investmentmanager

import com.embabel.agent.api.annotation.AchievesGoal
import com.embabel.agent.api.annotation.Action
import com.embabel.agent.api.annotation.Agent
import com.embabel.agent.api.common.OperationContext
import com.embabel.example.investmentmanager.dto.*
import com.embabel.example.investmentmanager.model.*
import com.embabel.example.investmentmanager.repository.CustomerAccountRepository
import com.embabel.example.investmentmanager.repository.PortfolioRepository
import com.embabel.example.investmentmanager.repository.PositionRepository
import org.springframework.stereotype.Service

@Agent(description = "Investment Manager Agent")
@Service
class InvestmentManagerAgent(
        private val accountRepository: CustomerAccountRepository,
        private val portfolioRepository: PortfolioRepository,
        private val positionRepository: PositionRepository
) {

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
    fun getCustomerId(request: InvestmentEnquiryRequest, context: OperationContext): CustomerId {
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
            throw IllegalArgumentException("Customer ID is required but was not provided.")
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
        val positions = portfolios.flatMap { positionRepository.findByPortfolioId(it.portfolioId) }
        return InvestmentList(positions)
    }

    @Action
    fun getTransactions(customerId: CustomerId, context: OperationContext): TransactionList {
        // Transactions not yet in SQL, returning empty list
        return TransactionList(emptyList())
    }

    @Action
    @AchievesGoal(
            description =
                    "Get details about portfolio holdings, stock performance, or assets. Use this for questions like 'what am I holding', 'how are my stocks doing', 'performance' or 'investments'."
    )
    fun explainInvestment(
            request: InvestmentEnquiryRequest,
            investments: InvestmentList,
            context: OperationContext
    ): String {
        val investmentSummary =
                investments.investments.joinToString("\n") {
                    "${it.assetDetails.ticker} (${it.assetDetails.instrumentType}): Qty: ${it.holdingData.quantity}, MarketVal: ${it.holdingData.marketValue}, P/L: ${it.performance.unrealizedGainLoss}"
                }

        val aiExplanation =
                context.ai()
                        .withAutoLlm()
                        .createObject(
                                "Based on these positions:\n$investmentSummary\n\n" +
                                        "Answer the user's question: ${request.userQuery}. " +
                                        "Output your response strictly as valid HTML paragraphs (<p>). Do not use Markdown.",
                                String::class.java
                        ) as
                        String

        // Construct HTML Table for holdings
        val holdingsHtml =
                investments.investments.joinToString(
                        separator = "",
                        prefix = "<div class='holdings-grid'>",
                        postfix = "</div>"
                ) { holding ->
                    val pnlClass = if (holding.performance.unrealizedGainLoss >= 0) "pos" else "neg"
                    val pnlSign = if (holding.performance.unrealizedGainLoss >= 0) "+" else ""
                    """
            <div class="holding-card">
                <div class="holding-header">
                    <span class="ticker">${holding.assetDetails.ticker}</span>
                    <span class="pnl $pnlClass">$pnlSign${holding.performance.unrealizedGainLoss}</span>
                </div>
                <div class="holding-body">
                   <div class="row"><span>Shares</span> <span>${holding.holdingData.quantity}</span></div>
                   <div class="row"><span>Mkt Val</span> <span>$${holding.holdingData.marketValue}</span></div>
                </div>
            </div>
            """.trimIndent()
                }

        return """
            $aiExplanation
            $holdingsHtml
        """.trimIndent()
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
            offers: OfferList,
            context: OperationContext
    ): String {
        val totalValuation = investments.investments.sumOf { it.holdingData.marketValue }
        val summary =
                context.ai()
                        .withAutoLlm()
                        .createObject(
                                "Create a premium portfolio summary for ${account.owner.legalName}. " +
                                        "Total valuation: $totalValuation. " +
                                        "Output STRICTLY as HTML content (using <p>, <strong> etc). Do not involve Markdown or code blocks.",
                                String::class.java
                        ) as
                        String

        // Build HTML Report manually to ensure perfect structure
        val txHtml =
                transactions.transactions.take(5).joinToString("") { tx ->
                    val colorClass = if (tx.amount >= 0) "pos" else "neg"
                    val sign = if (tx.amount >= 0) "+" else ""
                    """
            <div class="tx-item">
                <span class="tx-desc">${tx.description}</span>
                <span class="tx-amount $colorClass">$sign$${Math.abs(tx.amount)}</span>
            </div>
            """
                }

        val offersHtml =
                offers.offers.joinToString("") { offer ->
                    """
            <div class="offer-mini-card">
               <span class="offer-badge">${offer.reward}</span>
               <div class="offer-title">${offer.title}</div>
            </div>
            """
                }

        return """
            $summary
            <div class="report-container">
                <div class="report-section account-card">
                     <div class="report-header">Account Overview</div>
                     <div class="account-details">
                       <div class="acc-row"><span class="label">Owner</span> <span class="value">${account.owner.legalName}</span></div>
                       <div class="acc-row"><span class="label">Cash</span> <span class="value highlight">$${account.balances.settledCash}</span></div>
                     </div>
                </div>
                <div class="report-section">
                    <div class="report-header">Recent Activity</div>
                    <div class="tx-list">$txHtml</div>
                </div>
                <div class="report-section">
                    <div class="report-header">Recommended</div>
                    <div class="offers-grid">$offersHtml</div>
                </div>
            </div>
        """.trimIndent()
    }
}
