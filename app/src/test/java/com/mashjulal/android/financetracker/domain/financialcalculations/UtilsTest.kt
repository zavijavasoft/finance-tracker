package com.mashjulal.android.financetracker.domain.financialcalculations

import com.mashjulal.android.financetracker.formatCurrency
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.math.BigDecimal

private const val SYMBOL_RUBLE = "\u20BD"
private const val SYMBOL_DOLLAR = "$"

class UtilsTest {

    @Test
    fun `Test formatCurrency() with rubles`() {
        val money = Money(BigDecimal.ONE, Currency.RUBLE)

        val expected = "1.00 $SYMBOL_RUBLE"
        val actual = formatCurrency(money)

        assertEquals(expected, actual)
    }

    @Test
    fun `Test formatCurrency() with dollar`() {
        val money = Money(BigDecimal.ONE, Currency.DOLLAR)

        val expected = "1.00 $SYMBOL_DOLLAR"
        val actual = formatCurrency(money)

        assertEquals(expected, actual)
    }

    @Test
    fun `Test formatCurrency() with amount above 0`() {
        val money = Money(BigDecimal.TEN.asMoney(), Currency.DOLLAR)

        val expected = "10.00 $SYMBOL_DOLLAR"
        val actual = formatCurrency(money)

        assertEquals(expected, actual)
    }

    @Test
    fun `Test formatCurrency() with amount above 1000`() {
        val money = Money(BigDecimal.valueOf(9999), Currency.DOLLAR)

        val expected = "9,999.00 $SYMBOL_DOLLAR"
        val actual = formatCurrency(money)

        assertEquals(expected, actual)
    }

    @Test
    fun `Test formatCurrency() with amount below 0`() {
        val money = Money(BigDecimal.valueOf(-12), Currency.DOLLAR)

        val expected = "-12.00 $SYMBOL_DOLLAR"
        val actual = formatCurrency(money)

        assertEquals(expected, actual)
    }

    @Test
    fun `Test formatCurrency() with amount below -1000`() {
        val money = Money(BigDecimal.valueOf(-1200), Currency.DOLLAR)

        val expected = "-1,200.00 $SYMBOL_DOLLAR"
        val actual = formatCurrency(money)

        assertEquals(expected, actual)
    }

    @Test
    fun `Test formatCurrency() with amount equals to 0`() {
        val money = Money(BigDecimal.ZERO, Currency.DOLLAR)

        val expected = "0.00 $SYMBOL_DOLLAR"
        val actual = formatCurrency(money)

        assertEquals(expected, actual)
    }

    @Test
    fun `Test formatCurrency() with decimal amount`() {
        val money = Money(BigDecimal.valueOf(12.34), Currency.DOLLAR)

        val expected = "12.34 $SYMBOL_DOLLAR"
        val actual = formatCurrency(money)

        assertEquals(expected, actual)
    }
}