package com.embabel.example.investmentmanager.dto

import com.embabel.example.investmentmanager.model.CustomerAccount
import com.embabel.example.investmentmanager.model.Position
import com.embabel.example.investmentmanager.model.Transaction
import com.embabel.example.investmentmanager.model.Offer

interface InvestmentServiceResponse {
    val message: String
}

data class ExplanationResponse(override val message: String) : InvestmentServiceResponse
data class IssueResponse(override val message: String, val ticketId: String) : InvestmentServiceResponse
data class ReportResponse(override val message: String, val report: FullInvestmentReport) : InvestmentServiceResponse

data class FullInvestmentReport(
    val account: CustomerAccount,
    val positions: List<Position>,
    val transactions: List<Transaction>,
    val offers: List<Offer>,
    val aiSummary: String
)
