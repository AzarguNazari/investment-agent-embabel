package com.embabel.example.investmentmanager.model

data class Transaction(
    val id: String,
    val date: String,
    val description: String,
    val amount: Double,
    val type: String // BUY, SELL, DIVIDEND
)

data class Offer(val id: String, val title: String, val description: String, val reward: String)

data class InvestmentIssue(
    val id: String,
    val category: String, // VALUATION, MISSING_TRANSACTION, WITHDRAWAL, OTHER
    val description: String,
    val status: String = "OPEN"
)
