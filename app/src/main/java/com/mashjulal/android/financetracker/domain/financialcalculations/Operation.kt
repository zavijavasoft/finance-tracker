package com.mashjulal.android.financetracker.domain.financialcalculations

import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

/**
 * Class for representation financial operations.
 * Consists of operation type (incomings, outgoings),
 * money amount, currency.
 *
 * NOTE!! Every negative money amount will be converted to positive value.
 */
sealed class Operation(
        open val operationType: OperationType,
        open val amount: Money,
        open val category: Category,
        open val date: Date,
        open val account: Account
)

data class IncomingsOperation(
        override val amount: Money,
        override val category: Category,
        override val date: Date,
        override val account: Account
) : Operation(OperationType.INCOMINGS, amount, category, date, account)

data class OutgoingsOperation(
        override val amount: Money,
        override val category: Category,
        override val date: Date,
        override val account: Account
) : Operation(OperationType.OUTGOINGS, amount, category, date, account)

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