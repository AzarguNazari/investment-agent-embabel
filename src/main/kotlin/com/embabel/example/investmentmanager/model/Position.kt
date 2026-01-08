package com.embabel.example.investmentmanager.model

import jakarta.persistence.*

@Entity
@Table(name = "positions")
data class Position(
    @Id @Column(name = "position_id") val positionId: String,
    @Column(name = "portfolio_id") val portfolioId: String,
    @Embedded val assetDetails: AssetDetails,
    @Embedded val holdingData: HoldingData,
    @Embedded val performance: PositionPerformance
) {
    protected constructor() : this("", "", AssetDetails(), HoldingData(), PositionPerformance())
}

@Embeddable
data class AssetDetails(
    val ticker: String = "",
    val instrumentType: String = "",
    val exchange: String = "",
    val isin: String = ""
)

@Embeddable
data class HoldingData(
    val quantity: Double = 0.0,
    val averageBuyPrice: Double = 0.0,
    val costBasis: Double = 0.0,
    val currentMarketPrice: Double = 0.0,
    val marketValue: Double = 0.0
)

@Embeddable
data class PositionPerformance(
    val unrealizedGainLoss: Double = 0.0,
    val unrealizedGainLossPct: Double = 0.0,
    val weightInPortfolio: Double = 0.0
)
