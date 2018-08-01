package com.mashjulal.android.financetracker.domain.interactor

import com.mashjulal.android.financetracker.domain.financialcalculations.IncomingsOperation
import com.mashjulal.android.financetracker.domain.financialcalculations.Money
import com.mashjulal.android.financetracker.domain.financialcalculations.Operation
import com.mashjulal.android.financetracker.domain.financialcalculations.OutgoingsOperation
import com.mashjulal.android.financetracker.domain.repository.BalanceRepository
import com.mashjulal.android.financetracker.domain.repository.CurrencyRepository
import com.mashjulal.android.financetracker.domain.repository.OperationRepository
import io.reactivex.Completable

interface AddOperationInteractor {

    fun execute(operation: Operation): Completable
}

class AddOperationInteractorImpl(
        private val balanceRepository: BalanceRepository,
        private var currencyRepository: CurrencyRepository,
        private var operationRepository: OperationRepository
) : AddOperationInteractor {

    override fun execute(operation: Operation): Completable =
            Completable.fromAction {
                val balance = balanceRepository.getLastByAccount(operation.account)
                if (balance.amount.currency == operation.amount.currency) {
                    operationRepository.insert(operation)
                    return@fromAction
                }
                val rate = currencyRepository.getRate(operation.amount.currency.rate,
                        balance.amount.currency.rate)
                val convertedMoney = Money(operation.amount * rate, balance.amount.currency)
                val o = when (operation) {
                    is IncomingsOperation -> operation.copy(amount = convertedMoney)
                    is OutgoingsOperation -> operation.copy(amount = convertedMoney)
                }
                operationRepository.insert(o)
            }
}