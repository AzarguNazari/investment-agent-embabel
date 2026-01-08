package com.embabel.example.investmentmanager.model

import jakarta.persistence.*

@Entity
@Table(name = "transactions")
data class Transaction(
        @Id @Column(name = "transaction_id") val transactionId: String,
        @Column(name = "account_id") val accountId: String,
        val date: String,
        val type: String,
        val amount: Double,
        val description: String,
        val status: String
) {
    protected constructor() : this("", "", "", "", 0.0, "", "")
}
