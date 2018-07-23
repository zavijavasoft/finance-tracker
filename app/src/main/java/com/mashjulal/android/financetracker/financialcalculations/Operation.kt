package com.mashjulal.android.financetracker.financialcalculations

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.*

/**
 * Class for representation financial operations.
 * Consists of operation type (incomings, outgoings),
 * money amount, currency.
 *
 * NOTE!! Every negative money amount will be converted to positive value.
 */
class Operation(
        val operationType: OperationType,
        moneyAmount: BigDecimal,
        val currency: Currency
) {

    val amount: BigDecimal = moneyAmount.abs().setScale(2, RoundingMode.HALF_EVEN)

    override fun toString(): String {
        return NumberFormat
                .getCurrencyInstance(currency.locale)
                .format(amount)!!
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Operation) return false

        if (operationType != other.operationType) return false
        if (currency != other.currency) return false
        if (amount != other.amount) return false

        return true
    }

    override fun hashCode(): Int {
        var result = operationType.hashCode()
        result = 31 * result + currency.hashCode()
        result = 31 * result + amount.hashCode()
        return result
    }
}

/**
 * Enumeration class of currencies.
 * Each enum provides with it's locale.
 */
enum class Currency(val locale: Locale) {
    RUBLE(Locale("RU", "RU")),
    DOLLAR(Locale.US)
}

/**
 * Enumeration class of operation types.
 */
enum class OperationType {
    INCOMINGS, OUTGOINGS
}