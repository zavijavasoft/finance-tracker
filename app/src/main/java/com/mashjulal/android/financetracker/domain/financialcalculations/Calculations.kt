package com.mashjulal.android.financetracker.domain.financialcalculations

import java.math.BigDecimal

// Current dollar rate (at 22.07.18)
// TODO: remove hardcode
private val DOLLAR_RATE: BigDecimal = BigDecimal.valueOf(63.47)
        .asMoney()

/**
 * Converts rubles to dollars according to current rate.
 * @param money amount in rubles
 * @return money amount in dollars
 */
fun convertRublesToDollars(money: Money) = Money(money / DOLLAR_RATE, Currency.DOLLAR)

/**
 * Converts dollars to rubles according to current rate.
 * @param money amount in dollars
 * @return money amount in rubles
 */
fun convertDollarsToRubles(money: Money) = Money(money * DOLLAR_RATE, Currency.RUBLE)

/**
 * Calculates total money amount after all operations.
 * @param operations list of operations
 * @return result of operations
 */
fun calculateTotal(operations: List<Operation>): Money {
    if (operations.isEmpty()) {
        return Money(BigDecimal.ZERO, Currency.RUBLE)
    }

    var total = Money(BigDecimal.ZERO, operations[0].amount.currency)

    for (operation in operations) {
        val amount = (when (operation) {
            is IncomingsOperation -> operation.amount
            is OutgoingsOperation -> -operation.amount
        })
        total += amount
    }
    return total
}

/**
 * Calculates total money amount after all operations.
 * @param balances list of balances
 * @return total sum of balances
 */
fun calculateBalance(balances: List<Balance>): Money {
    if (balances.isEmpty()) {
        return Money(BigDecimal.ZERO, Currency.RUBLE)
    }

    var total = Money(BigDecimal.ZERO, balances[0].amount.currency)

    for (balance in balances) {
        total += balance.amount
    }
    return total
}