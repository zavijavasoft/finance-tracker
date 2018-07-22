package com.mashjulal.android.financetracker

import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

/**
 * Returns currency string representation with correct symbol.
 * @param money money amount
 * @param countryCode country
 * @return currency string representation
 */
fun formatCurrency(money: BigDecimal, countryCode: String) = NumberFormat
        .getCurrencyInstance(Locale(countryCode, countryCode))
        .format(money)!!

// Current dollar rate (at 22.07.18)
// TODO: remove hardcode
private val DOLLAR_RATE: BigDecimal = BigDecimal.valueOf(63.47)

/**
 * Converts rubles to dollars according to current rate.
 * @param money amount in rubles
 * @return money amount in dollars
 */
fun convertRublesToDollars(money: BigDecimal) =
        money / DOLLAR_RATE