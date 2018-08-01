package com.mashjulal.android.financetracker.domain.financialcalculations

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
            .map { if (it is OutgoingsOperation) -it.amount else it.amount }
            .reduce { acc, money -> acc + money }
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


