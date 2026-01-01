package com.embabel.example.investmentmanager.model

import jakarta.persistence.*

@Entity
@Table(name = "portfolios")
data class Portfolio(
    @Id
    @Column(name = "portfolio_id")
    val portfolioId: String,
    @Column(name = "linked_account_id")
    val linkedAccountId: String,
    @Embedded
    val metadata: PortfolioMetadata,
    @Embedded
    val strategy: PortfolioStrategy,
    @Embedded
    val valuation: PortfolioValuation
) {
    protected constructor() : this("", "", PortfolioMetadata(), PortfolioStrategy(), PortfolioValuation())
}

@Embeddable
data class PortfolioMetadata(
    val displayName: String = "",
    val inceptionDate: String = "",
    val managementStyle: String = ""
)

@Embeddable
data class PortfolioStrategy(
    val targetRiskScore: Int = 0,
    val primaryBenchmark: String = "",
    val rebalancingFrequency: String = ""
)

@Embeddable
data class PortfolioValuation(
    val totalMarketValue: Double = 0.0,
    val totalUnrealizedPnl: Double = 0.0,
    val dailyChangePct: Double = 0.0
)
