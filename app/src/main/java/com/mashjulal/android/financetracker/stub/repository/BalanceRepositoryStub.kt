package com.mashjulal.android.financetracker.stub.repository

import com.mashjulal.android.financetracker.domain.financialcalculations.Account
import com.mashjulal.android.financetracker.domain.financialcalculations.Balance
import com.mashjulal.android.financetracker.domain.financialcalculations.Currency
import com.mashjulal.android.financetracker.domain.financialcalculations.Money
import com.mashjulal.android.financetracker.domain.repository.BalanceRepository
import io.reactivex.Single
import java.math.BigDecimal
import java.util.*

// TODO: remove hardcode
private val calendar = Calendar.getInstance()
private val tomorrow = Date(calendar.timeInMillis + 1000 * 60 * 60 * 24)

class BalanceRepositoryStub : BalanceRepository {

    private val data: HashMap<Int, Balance> = hashMapOf(
            1 to Balance(Account("John Smith"), Money(BigDecimal.ZERO, Currency.RUBLE), tomorrow),
            2 to Balance(Account("John Smith Credit Card"), Money(BigDecimal.ZERO, Currency.RUBLE), tomorrow)
    )

    override fun getByAccount(account: Account): Single<List<Balance>> {
        return Single.just(data.asSequence().map { it.value }.filter { it.account == account }.toList())
    }

    override fun getByAccountAfter(account: Account, date: Date): Single<List<Balance>> {
        return Single.just(data.asSequence().map { it.value }.filter { it.account == account && it.date.after(date) }.toList())
    }

    override fun getLastByAccount(account: Account): Single<List<Balance>> {
        return Single.just(listOf(data.asSequence().map { it.value }.last { it.account == account }))
    }

    override fun getLastByAll(): Single<List<Balance>> {
        return Single.just(data.asSequence().map { it.value }.groupBy { it.account }.map { it.value.last() })
    }
}