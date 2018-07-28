package com.mashjulal.android.financetracker.financialcalculations

import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Class for representation financial operations.
 * Consists of operation type (incomings, outgoings),
 * money amount, currency.
 *
 * NOTE!! Every negative money amount will be converted to positive value.
 */
data class Operation(
        val operationType: OperationType,
        private val _amount: BigDecimal,
        val currency: Currency
) {

    val amount: BigDecimal = _amount.abs().asMoney()
}

/**
 * Enumeration class of currencies.
 * Each enum provides with it's locale.
 */
enum class Currency(val symbol: String) {
    RUBLE("\u20BD"),
    DOLLAR("$")
}

/**
 * Enumeration class of operation types.
 */
enum class OperationType {
    INCOMINGS, OUTGOINGS
}

fun BigDecimal.asMoney() = setScale(2, RoundingMode.HALF_EVEN)!!