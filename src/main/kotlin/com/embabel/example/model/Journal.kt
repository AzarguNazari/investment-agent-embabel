package com.embabel.example.model

import jakarta.persistence.*

@Entity
@Table(name = "journals")
data class JournalEntry(
        @Id @Column(name = "journal_id") val journalId: String,
        @Column(name = "account_id") val accountId: String,
        @Column(name = "entry_date") val entryDate: String,
        val debitAccount: String,
        val creditAccount: String,
        val amount: Double,
        val description: String
) {
    protected constructor() : this("", "", "", "", "", 0.0, "")
}
