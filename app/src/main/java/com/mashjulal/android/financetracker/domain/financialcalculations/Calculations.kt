package com.mashjulal.android.financetracker.domain.financialcalculations

import io.reactivex.Observable
import io.reactivex.Single
import java.math.BigDecimal

/**
 * Calculates total money amount after all operations.
 * @param operations list of operations
 * @return result of operations
 */
fun calculateTotal(operations: List<Operation>, defaultCurrency: Currency): Money {
    if (operations.isEmpty())
        return Money(BigDecimal.ZERO, defaultCurrency)
    return operations
            .map { if (it.category.operationType == OperationType.OUTGOINGS) -it.amount else it.amount }
            .reduce { acc, money -> acc + money }
}


fun calculateTotalEx(operations: List<Operation>, defaultCurrency: Currency,
                     converter: (from: String, to: String) -> Single<BigDecimal>): Single<Money> {
    return Observable.fromIterable(operations)
            .flatMap { opi ->
                converter(opi.amount.currency.rate, defaultCurrency.rate)
                        .flatMap { converted: BigDecimal ->
                            Single.fromCallable {
                                val newOps = opi.copy(amount = Money(converted, defaultCurrency) * opi.amount.amount)
                                newOps
                            }
                        }.toObservable()
            }
            .map {
                val x = if (it.category.operationType == OperationType.OUTGOINGS) -it.amount else it.amount
                Money(x, defaultCurrency)
            }.reduce { acc, money -> acc + money }.toSingle(Money(BigDecimal.ZERO, defaultCurrency))
}


/**
 * Calculates total money amount after all operations.
 * @param balances list of balances
 * @return total sum of balances
 */
fun calculateBalance(balances: List<Balance>, defaultCurrency: Currency): Money {
    if (balances.isEmpty())
        return Money(BigDecimal.ZERO, defaultCurrency)
    return balances
            .map { it.amount }
            .reduce { acc, money -> acc + money }
}


