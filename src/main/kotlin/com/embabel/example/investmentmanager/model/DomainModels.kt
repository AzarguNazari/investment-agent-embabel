package com.embabel.example.investmentmanager.model

data class Offer(val id: String, val title: String, val description: String, val reward: String)

data class InvestmentIssue(
        val id: String,
        val category: String, // VALUATION, MISSING_TRANSACTION, WITHDRAWAL, OTHER
        val description: String,
        val status: String = "OPEN"
)
