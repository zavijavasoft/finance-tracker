package com.mashjulal.android.financetracker.domain.financialcalculations

import java.math.BigDecimal

data class Money(
        private val _amount: BigDecimal,
        val currency: Currency
) {
    var amount: BigDecimal = _amount.asMoney()

    constructor(money: Money, currency: Currency) : this(money.amount, currency)

    operator fun plus(other: Money): Money {
        return if (currency == other.currency) {
            Money(amount + other.amount, currency)
        } else {
            if (currency == Currency.DOLLAR) {
                Money(amount + convertRublesToDollars(other).amount, Currency.DOLLAR)
            } else {
                Money(amount + convertDollarsToRubles(other).amount, Currency.RUBLE)
            }
        }
    }

    operator fun times(other: BigDecimal): Money = Money(amount * other, currency)

    operator fun div(other: BigDecimal): Money = Money(amount / other, currency)

    operator fun minus(other: Money) = this + -other

    operator fun unaryMinus(): Money = Money(-amount, currency)
}