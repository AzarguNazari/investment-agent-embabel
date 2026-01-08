package com.embabel.example.investmentmanager.model

import jakarta.persistence.*

@Entity
@Table(name = "documents")
data class Document(
        @Id @Column(name = "document_id") val documentId: String,
        @Column(name = "account_id") val accountId: String,
        val type: String,
        val title: String,
        @Column(name = "issue_date") val issueDate: String,
        val url: String
) {
    protected constructor() : this("", "", "", "", "", "")
}
