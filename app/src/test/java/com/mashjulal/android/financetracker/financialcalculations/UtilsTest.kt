package com.mashjulal.android.financetracker.financialcalculations

import com.mashjulal.android.financetracker.formatCurrency
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.math.BigDecimal

private const val SYMBOL_RUBLE = "\u20BD"
private const val SYMBOL_DOLLAR = "$"

class UtilsTest {

    @Test
    fun `Test formatCurrency() with rubles`() {
        val money = BigDecimal.ONE.asMoney()

        val expected = "1.00 $SYMBOL_RUBLE"
        val actual = formatCurrency(money, SYMBOL_RUBLE)

        assertEquals(expected, actual)
    }

    @Test
    fun `Test formatCurrency() with dollar`() {
        val money = BigDecimal.ONE.asMoney()

        val expected = "1.00 $SYMBOL_DOLLAR"
        val actual = formatCurrency(money, SYMBOL_DOLLAR)

        assertEquals(expected, actual)
    }

    @Test
    fun `Test formatCurrency() with amount above 0`() {
        val money = BigDecimal.TEN.asMoney()

        val expected = "10.00 $SYMBOL_DOLLAR"
        val actual = formatCurrency(money, SYMBOL_DOLLAR)

        assertEquals(expected, actual)
    }

    @Test
    fun `Test formatCurrency() with amount above 1000`() {
        val money = BigDecimal.valueOf(9999).asMoney()

        val expected = "9,999.00 $SYMBOL_DOLLAR"
        val actual = formatCurrency(money, SYMBOL_DOLLAR)

        assertEquals(expected, actual)
    }

    @Test
    fun `Test formatCurrency() with amount below 0`() {
        val money = BigDecimal.valueOf(-12).asMoney()

        val expected = "-12.00 $SYMBOL_DOLLAR"
        val actual = formatCurrency(money, SYMBOL_DOLLAR)

        assertEquals(expected, actual)
    }

    @Test
    fun `Test formatCurrency() with amount below -1000`() {
        val money = BigDecimal.valueOf(-1200).asMoney()

        val expected = "-1,200.00 $SYMBOL_DOLLAR"
        val actual = formatCurrency(money, SYMBOL_DOLLAR)

        assertEquals(expected, actual)
    }

    @Test
    fun `Test formatCurrency() with amount equals to 0`() {
        val money = BigDecimal.ZERO.asMoney()

        val expected = "0.00 $SYMBOL_DOLLAR"
        val actual = formatCurrency(money, SYMBOL_DOLLAR)

        assertEquals(expected, actual)
    }

    @Test
    fun `Test formatCurrency() with decimal amount`() {
        val money = BigDecimal.valueOf(12.34).asMoney()

        val expected = "12.34 $SYMBOL_DOLLAR"
        val actual = formatCurrency(money, SYMBOL_DOLLAR)

        assertEquals(expected, actual)
    }
}