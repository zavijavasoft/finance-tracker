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