package com.embabel.example.repository

import com.embabel.example.model.CustomerAccount
import com.embabel.example.model.Document
import com.embabel.example.model.JournalEntry
import com.embabel.example.model.Portfolio
import com.embabel.example.model.Position
import com.embabel.example.model.Transaction
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CustomerAccountRepository : CrudRepository<CustomerAccount, String> {
}

@Repository
interface PortfolioRepository : CrudRepository<Portfolio, String> {
    fun findByLinkedAccountId(linkedAccountId: String): List<Portfolio>
}

@Repository
interface PositionRepository : CrudRepository<Position, String> {
    fun findByPortfolioId(portfolioId: String): List<Position>
}

@Repository
interface TransactionRepository : CrudRepository<Transaction, String> {
    fun findByAccountId(accountId: String): List<Transaction>
}

@Repository
interface JournalRepository : CrudRepository<JournalEntry, String> {
    fun findByAccountId(accountId: String): List<JournalEntry>
}

@Repository
interface DocumentRepository : CrudRepository<Document, String> {
    fun findByAccountId(accountId: String): List<Document>
}
