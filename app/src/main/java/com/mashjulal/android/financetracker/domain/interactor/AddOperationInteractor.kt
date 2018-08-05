package com.mashjulal.android.financetracker.domain.interactor

import com.mashjulal.android.financetracker.domain.financialcalculations.Balance
import com.mashjulal.android.financetracker.domain.financialcalculations.Money
import com.mashjulal.android.financetracker.domain.financialcalculations.Operation
import com.mashjulal.android.financetracker.domain.financialcalculations.OperationType
import com.mashjulal.android.financetracker.domain.repository.AccountRepository
import com.mashjulal.android.financetracker.domain.repository.BalanceRepository
import com.mashjulal.android.financetracker.domain.repository.CurrencyRepository
import com.mashjulal.android.financetracker.domain.repository.OperationRepository
import io.reactivex.Completable
import java.math.BigDecimal

interface AddOperationInteractor {

    fun execute(operation: Operation): Completable
}

class AddOperationInteractorImpl(
        private val balanceRepository: BalanceRepository,
        private var accoutRepository: AccountRepository,
        private var currencyRepository: CurrencyRepository,
        private var operationRepository: OperationRepository
) : AddOperationInteractor {

    override fun execute(operation: Operation): Completable =
            balanceRepository.getLastByAccount(operation.account)
                    .flatMap { balance: List<Balance> ->
                        currencyRepository.getRate(operation.amount.currency.rate,
                                balance[0].amount.currency.rate)
                                .map { rate: BigDecimal ->
                                    operation.copy(account = balance[0].account, ratio = rate.toDouble())
                                }
                    }.flatMapCompletable {
                        val sign = if (it.category.operationType == OperationType.INCOMINGS) 1 else -1
                        val convertedMoney = Money(it.amount * BigDecimal(it.ratio),
                                it.account.money.currency) * BigDecimal(sign)

                        val newMoney = it.account.money + convertedMoney

                        val updateAccount = accoutRepository.update(operation.account.title, newMoney)

                        operationRepository.insert(it).andThen(updateAccount)
                    }
}