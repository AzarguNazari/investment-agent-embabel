package com.embabel.example.investmentmanager.dto

import com.embabel.example.investmentmanager.model.Position
import com.embabel.example.investmentmanager.model.Offer
import com.embabel.example.investmentmanager.model.Transaction

data class InvestmentEnquiryRequest(val userQuery: String)
data class CustomerId(val id: String)

data class InvestmentList(val investments: List<Position>)
data class OfferList(val offers: List<Offer>)
data class TransactionList(val transactions: List<Transaction>)

data class InvestmentDirectResponse(
    val content: String,
    val type: String = "text", // "text", "holdings", "report"
    val data: Any? = null
)
