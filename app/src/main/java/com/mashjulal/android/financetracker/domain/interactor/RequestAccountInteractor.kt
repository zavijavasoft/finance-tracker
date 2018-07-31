package com.mashjulal.android.financetracker.domain.interactor

import com.mashjulal.android.financetracker.domain.financialcalculations.Account
import com.mashjulal.android.financetracker.domain.repository.AccountRepository
import io.reactivex.Observable

interface RequestAccountInteractor {
    fun execute(): Observable<List<Account>>
}

class RequestAccountInteractorImpl(
        private val accountRepository: AccountRepository
) : RequestAccountInteractor {

    override fun execute(): Observable<List<Account>> = Observable.create {
        it.onNext(accountRepository.getAll())
    }

}