package com.mashjulal.android.financetracker.domain.interactor

import com.mashjulal.android.financetracker.domain.financialcalculations.*
import com.mashjulal.android.financetracker.domain.repository.AccountRepository
import com.mashjulal.android.financetracker.domain.repository.BalanceRepository
import com.mashjulal.android.financetracker.domain.repository.CurrencyRepository
import com.mashjulal.android.financetracker.domain.repository.OperationRepository
import io.reactivex.Completable
import java.math.BigDecimal
import java.util.*

interface AddOperationInteractor {

    fun execute(operation: Operation): Completable
    fun executePlanned(): Completable
}

class AddOperationInteractorImpl(
        private val balanceRepository: BalanceRepository,
        private var accoutRepository: AccountRepository,
        private var currencyRepository: CurrencyRepository,
        private var operationRepository: OperationRepository
) : AddOperationInteractor {


    override fun executePlanned(): Completable {


        return operationRepository.getAll()
                .map { it -> it.filter { it.repeator != Repeator.REPEAT_NONE } }
                .flatMapCompletable {
                    var completable = Completable.complete()
                    val now = GregorianCalendar()
                    for (ops in it) {
                        val then = GregorianCalendar().apply { time = ops.date }
                        do {
                            then.apply {
                                when (ops.repeator) {
                                    Repeator.REPEAT_WEEKLY -> add(Calendar.DAY_OF_MONTH, 7)
                                    Repeator.REPEAT_MONTHLY -> add(Calendar.MONTH, 1)
                                    else -> add(Calendar.YEAR, 100)
                                }
                            }
                            if (then.before(now)) {
                                val update = operationRepository.update(ops.copy(repeator = Repeator.REPEAT_NONE))
                                val insert = operationRepository.insert(ops.copy())
                                completable = completable.andThen(update).andThen(insert)
                            }
                        } while (then.before(now))
                    }
                    completable
                }

    }

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