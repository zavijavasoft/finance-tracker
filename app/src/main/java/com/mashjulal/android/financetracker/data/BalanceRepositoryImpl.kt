package com.mashjulal.android.financetracker.data

import com.mashjulal.android.financetracker.domain.financialcalculations.Account
import com.mashjulal.android.financetracker.domain.financialcalculations.Balance
import com.mashjulal.android.financetracker.domain.financialcalculations.Currency
import com.mashjulal.android.financetracker.domain.financialcalculations.Money
import com.mashjulal.android.financetracker.domain.repository.BalanceRepository
import java.math.BigDecimal
import java.util.*

// TODO: remove hardcode
private val calendar = Calendar.getInstance()
private val account = Account("John Smith")
private val tomorrow = Date(calendar.timeInMillis + 1000 * 60 * 60 * 24)

class BalanceRepositoryImpl : BalanceRepository {

    private val data: HashMap<Int, Balance> = hashMapOf(
            1 to Balance(account, Money(BigDecimal.ZERO, Currency.RUBLE), tomorrow)
    )

    override fun getByAccount(account: Account): List<Balance> {
        return data.asSequence().map { it.value }.filter { it.account == account }.toList()
    }

    override fun getByAccountAfter(account: Account, date: Date): List<Balance> {
        return data.asSequence().map { it.value }.filter { it.account == account && it.date.after(date) }.toList()
    }

    override fun getLastByAccount(account: Account): Balance {
        return data.asSequence().map { it.value }.last { it.account == account }
    }

    override fun getLastByAll(): List<Balance> {
        return data.asSequence().map { it.value }.groupBy { it.account }.map { it.value.last() }
    }
}