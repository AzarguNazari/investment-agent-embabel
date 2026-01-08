package com.embabel.example.investmentmanager.repository

import com.embabel.example.investmentmanager.model.*
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CustomerAccountRepository : CrudRepository<CustomerAccount, String> {
    fun findByOwnerUserId(userId: String): CustomerAccount?
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
