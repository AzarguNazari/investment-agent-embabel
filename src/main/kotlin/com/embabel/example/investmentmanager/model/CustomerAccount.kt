package com.embabel.example.investmentmanager.model

import jakarta.persistence.*

@Entity
@Table(name = "accounts")
data class CustomerAccount(
        @Id @Column(name = "account_id") val accountId: String,
        val status: String,
        val type: String,
        @Embedded val owner: AccountOwner,
        @Embedded val bankAccount: ServiceBankAccount,
        @Embedded val balances: AccountBalances,
        @Embedded val complianceFlags: ComplianceFlags
) {
    protected constructor() :
            this(
                    "",
                    "",
                    "",
                    AccountOwner(),
                    ServiceBankAccount(),
                    AccountBalances(),
                    ComplianceFlags()
            )
}

@Embeddable
data class AccountOwner(
        @Column(name = "owner_user_id") val userId: String = "",
        val legalName: String = "",
        val email: String = "",
        val phone: String = "",
        val address: String = "",
        val taxIdentificationNumber: String = "",
        val domicile: String = ""
)

@Embeddable
data class ServiceBankAccount(
        @Column(name = "bank_name") val bankName: String = "",
        val iban: String = "",
        val bic: String = ""
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
