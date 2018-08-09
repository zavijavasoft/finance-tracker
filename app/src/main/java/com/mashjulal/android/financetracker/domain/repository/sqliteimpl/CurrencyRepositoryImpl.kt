package com.mashjulal.android.financetracker.domain.repository.sqliteimpl

import com.mashjulal.android.financetracker.data.currencyconvertapi.CurrencyService
import com.mashjulal.android.financetracker.domain.financialcalculations.Currency
import com.mashjulal.android.financetracker.domain.repository.CurrencyRepository
import io.reactivex.Single
import java.math.BigDecimal
import java.util.*
import javax.inject.Inject

data class Exchange(val from: String,
                    val to: String,
                    val ratio: BigDecimal,
                    val lastUpdated: Date = Date(0))


class CurrencyRepositoryImpl @Inject constructor(
        private val currencyService: CurrencyService
) : CurrencyRepository {

    val map = mutableMapOf<String, Exchange>()

    override fun getRate(from: String, to: String): Single<BigDecimal> {
        if (from == to)
            return Single.just(BigDecimal.ONE)

        val q = "${from}_$to"

        map[q]?.let {
            val calendar = GregorianCalendar()
            calendar.add(Calendar.DAY_OF_MONTH, -1)
            val lastUpdated = GregorianCalendar().apply { time = it.lastUpdated }
            if (lastUpdated.after(calendar))
                return Single.just(it.ratio)

        }

        return currencyService.getRate(q)
                .doOnSuccess { map[q] = Exchange(from, to, it.rate, Date()) }
                .map { it.rate }
    }

    override fun getCurrencyList(): Single<List<Currency>> {
        return Single.just(listOf(Currency.DOLLAR, Currency.RUBLE, Currency.EURO))
    }
}