package com.mashjulal.android.financetracker.financialcalculations

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
fun convertRublesToDollars(money: BigDecimal) =
        (money / DOLLAR_RATE).asMoney()

/**
 * Converts dollars to rubles according to current rate.
 * @param money amount in dollars
 * @return money amount in rubles
 */
fun convertDollarsToRubles(money: BigDecimal) =
        (money * DOLLAR_RATE).asMoney()

/**
 * Calculates total money amount after all operations.
 * @param operations list of operations
 * @return result of operations
 */
fun calculateTotal(operations: List<Operation>): BigDecimal {
    var total = BigDecimal.ZERO.asMoney()

    for (operation in operations) {
        val amount = when (operation.currency) {
            Currency.RUBLE -> operation.amount
            Currency.DOLLAR -> convertDollarsToRubles(operation.amount)
        }
        total += (when (operation.operationType) {
            OperationType.INCOMINGS -> amount
            OperationType.OUTGOINGS -> -amount
        }).asMoney()
    }

    return total
}