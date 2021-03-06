package com.mashjulal.android.financetracker.stub.repository

import com.mashjulal.android.financetracker.data.currencyconvertapi.CurrencyService
import com.mashjulal.android.financetracker.domain.financialcalculations.Currency
import com.mashjulal.android.financetracker.domain.repository.CurrencyRepository
import io.reactivex.Single
import java.math.BigDecimal
import javax.inject.Inject

class CurrencyRepositoryStub @Inject constructor(
        private val currencyService: CurrencyService
) : CurrencyRepository {
    override fun getRate(from: String, to: String): Single<BigDecimal> {
        val q = "${from}_$to"
        return currencyService.getRate(q).map { it.rate }
    }

    override fun getCurrencyList(): Single<List<Currency>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}