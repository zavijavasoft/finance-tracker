package com.mashjulal.android.financetracker.domain.financialcalculations

import java.math.BigDecimal

/**
 * Calculates total money amount after all operations.
 * @param operations list of operations
 * @param rate currency rate
 * @return result of operations
 */
fun calculateTotal(operations: List<Operation>, rate: BigDecimal = BigDecimal.ONE): Money {
    val amounts = operations.map { if (it is OutgoingsOperation) -it.amount else it.amount }
    if (amounts.isEmpty())
        return Money(BigDecimal.ZERO, Currency.RUBLE)
    return amounts.reduce { acc, money -> acc + (if (acc.currency != money.currency) money * rate else money) }
}
/**
 * Calculates total money amount after all operations.
 * @param balances list of balances
 * @param rate currency rate
 * @return total sum of balances
 */
fun calculateBalance(balances: List<Balance>, rate: BigDecimal = BigDecimal.ONE): Money {
    val amounts = balances.map { it.amount }
    if (amounts.isEmpty())
        return Money(BigDecimal.ZERO, Currency.RUBLE)
    return amounts.reduce { acc, money -> acc + (if (acc.currency != money.currency) money * rate else money) }
}


