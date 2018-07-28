package com.mashjulal.android.financetracker

import java.math.BigDecimal
import java.text.DecimalFormat
import java.util.*

/**
 * Returns currency string representation with correct symbol.
 * @param money money amount
 * @param currencySymbol symbol of chosen currency
 * @return currency string representation
 */
fun formatCurrency(money: BigDecimal, currencySymbol: String): String {
    val formatter = DecimalFormat.getCurrencyInstance(Locale.ENGLISH) as DecimalFormat
    formatter.decimalFormatSymbols = formatter.decimalFormatSymbols.apply {
        setCurrencySymbol("")
    }
    return "${formatter.format(money).trim()} $currencySymbol"
}