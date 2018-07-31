package com.mashjulal.android.financetracker

import com.mashjulal.android.financetracker.domain.financialcalculations.Money
import java.text.DecimalFormat
import java.util.*

/**
 * Returns currency string representation with correct symbol.
 * @param money money amount
 * @return currency string representation
 */
fun formatCurrency(money: Money): String {
    val formatter = DecimalFormat.getCurrencyInstance(Locale.ENGLISH) as DecimalFormat
    formatter.decimalFormatSymbols = formatter.decimalFormatSymbols.apply {
        currencySymbol = ""
    }
    return "${formatter.format(money.amount).trim()} ${money.currency.symbol}"
}