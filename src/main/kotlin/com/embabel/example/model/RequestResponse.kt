package com.embabel.example.model

data class InvestmentEnquiryRequest(val userQuery: String)

data class CustomerId(val id: String)

data class InvestmentList(val investments: List<Position>)

data class OfferList(val offers: List<Offer>)

data class TransactionList(val transactions: List<Transaction>)

data class JournalList(val journals: List<JournalEntry>)

data class DocumentList(val documents: List<Document>)
