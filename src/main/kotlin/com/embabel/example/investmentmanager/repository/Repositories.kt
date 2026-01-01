package com.embabel.example.investmentmanager.repository

import com.embabel.example.investmentmanager.model.CustomerAccount
import com.embabel.example.investmentmanager.model.Portfolio
import com.embabel.example.investmentmanager.model.Position
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
