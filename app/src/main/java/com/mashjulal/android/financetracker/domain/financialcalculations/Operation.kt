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
        val ratio: Double = 1.0,
        val repeator: Repeator = Repeator.REPEAT_NONE
)


data class Currency(var rate: String) {
    companion object {
        val RUBLE = Currency("RUB")
        val DOLLAR = Currency("USD")
        val EURO = Currency("EUR")
    }

    val symbol: String =
            when (rate) {
                "RUB" -> "\u20BD"
                "USD" -> "$"
                "EUR" -> "\u20AC"
                else -> "*"
            }
}

/**
 * Enumeration class of operation types.
 */
enum class OperationType {
    INCOMINGS, OUTGOINGS;

    companion object {
        fun fromString(stringType: String): OperationType {
            return when (stringType) {
                "INCOMINGS" -> INCOMINGS
                "OUTGOINGS" -> OUTGOINGS
                else -> OUTGOINGS
            }
        }

    }
}

enum class Repeator {
    REPEAT_NONE,
    REPEAT_WEEKLY,
    REPEAT_MONTHLY;


    companion object {
        fun getByString(stringType: String): Repeator {
            return when (stringType) {
                "REPEAT_NONE" -> REPEAT_NONE
                "REPEAT_WEEKLY" -> REPEAT_WEEKLY
                "REPEAT_MONTHLY" -> REPEAT_MONTHLY
                else -> REPEAT_NONE
            }
        }

    }
}


fun BigDecimal.asMoney(): BigDecimal = setScale(2, RoundingMode.HALF_EVEN)