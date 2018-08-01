package com.mashjulal.android.financetracker.domain.financialcalculations

import java.math.BigDecimal

data class Money(
        private val _amount: BigDecimal,
        val currency: Currency
) {
    val amount: BigDecimal = _amount.asMoney()

    constructor(money: Money, currency: Currency) : this(money.amount, currency)

    operator fun plus(other: Money): Money = Money(amount + other.amount, currency)

    operator fun times(other: BigDecimal): Money = Money(amount * other, currency)

    operator fun div(other: BigDecimal): Money = Money(amount / other, currency)

    operator fun minus(other: Money) = this + -other

    operator fun unaryMinus(): Money = Money(-amount, currency)

    fun abs(): Money = Money(amount.abs(), currency)
}