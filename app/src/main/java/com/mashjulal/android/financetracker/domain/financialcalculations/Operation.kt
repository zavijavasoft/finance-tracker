package com.mashjulal.android.financetracker.domain.financialcalculations

import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*


/**
 * Class for representation financial operations.
 * Consists of operation type (incomings, outgoings),
 * money amount, currency.
 *

 */
data class Operation(
        val id: Long = -1,
        val amount: Money = Money(BigDecimal.ZERO, Currency.RUBLE),
        val category: Category = Category(),
        val date: Date = Date(),
        val account: Account = Account(),
        val ratio: Double = 1.0
)

/*
data class IncomingsOperation(
) : Operation()

data class OutgoingsOperation(
        override val id : Long,
        override val amount: Money,
        override val category: Category,
        override val date: Date,
        override val account: Account
) : Operation(id, OperationType.OUTGOINGS, amount, category, date, account)
**/
/**
 * Enumeration class of currencies.
 * Each enum provides with it's locale.
 */
data class Currency(var rate: String) {
    companion object {
        val RUBLE = Currency("RUB")
        val DOLLAR = Currency("USD")
    }

    val symbol: String =
            when (rate) {
                "RUB" -> "\u20BD"
                "USD" -> "$"
                else -> "*"
            }
}

/**
 * Enumeration class of operation types.
 */
enum class OperationType {
    INCOMINGS, OUTGOINGS;

    companion object {
        fun getTypeByString(stringType: String): OperationType {
            return when (stringType) {
                "INCOMINGS" -> INCOMINGS
                "OUTGOINGS" -> OUTGOINGS
                else -> OUTGOINGS
            }
        }

    }
}

fun BigDecimal.asMoney(): BigDecimal = setScale(2, RoundingMode.HALF_EVEN)