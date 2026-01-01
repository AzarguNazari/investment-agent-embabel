package com.embabel.example.investmentmanager.model

import jakarta.persistence.*

@Entity
@Table(name = "accounts")
data class CustomerAccount(
    @Id
    @Column(name = "account_id")
    val accountId: String,
    val status: String,
    val type: String,
    @Embedded
    val owner: AccountOwner,
    @Embedded
    val balances: AccountBalances,
    @Embedded
    val complianceFlags: ComplianceFlags
) {
    protected constructor() : this("", "", "", AccountOwner(), AccountBalances(), ComplianceFlags())
}

@Embeddable
data class AccountOwner(
    @Column(name = "owner_user_id") val userId: String = "",
    val legalName: String = "",
    val taxIdentificationNumber: String = "",
    val domicile: String = ""
)

@Embeddable
data class AccountBalances(
    val settledCash: Double = 0.0,
    val buyingPower: Double = 0.0,
    val marginBalance: Double = 0.0,
    val currency: String = "USD"
)

@Embeddable
data class ComplianceFlags(
    val isPatternDayTrader: Boolean = false,
    val kycStatus: String = "",
    val suitabilityLevel: String = ""
)
