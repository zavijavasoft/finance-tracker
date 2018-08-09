package com.mashjulal.android.financetracker.domain.interactor

import com.mashjulal.android.financetracker.domain.financialcalculations.Account
import com.mashjulal.android.financetracker.domain.financialcalculations.Currency
import com.mashjulal.android.financetracker.domain.financialcalculations.Money
import com.mashjulal.android.financetracker.domain.repository.AccountRepository
import io.reactivex.Completable
import io.reactivex.Observable
import java.math.BigDecimal
import javax.inject.Inject

interface RequestAccountInteractor {
    fun execute(): Observable<List<Account>>

    fun updateAccount(title: String, currencyCharCode: String): Completable
}

class DispatchAccountInteractor @Inject constructor(
        private val accountRepository: AccountRepository
) : RequestAccountInteractor {

    override fun execute(): Observable<List<Account>> = accountRepository.getAll().toObservable()

    override fun updateAccount(title: String, currencyCharCode: String): Completable {
        return accountRepository.getByName(title)
                .flatMapCompletable {
                    if (it.isEmpty()) {
                        accountRepository.insert(title,
                                Money(BigDecimal.ZERO, Currency(currencyCharCode)))
                    } else {
                        Completable.complete()
                    }
                }
    }
}