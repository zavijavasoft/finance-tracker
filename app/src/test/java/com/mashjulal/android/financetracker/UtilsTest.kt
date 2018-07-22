package com.mashjulal.android.financetracker

import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal

/**
 * Test class for Utils functions.
 */
class UtilsTest {

    @Test
    fun `Test convertRublesToDollars with amount above 0`() {
        val amount = BigDecimal.valueOf(111.00)

        val expected = BigDecimal.valueOf(1.7)
        val actual = convertRublesToDollars(amount)

        assertEquals(expected, actual)
    }

    @Test
    fun `Test convertRublesToDollars with amount below 0`() {
        val amount = BigDecimal.valueOf(-111.00)

        val expected = BigDecimal.valueOf(-1.7)
        val actual = convertRublesToDollars(amount)

        assertEquals(expected, actual)
    }

    @Test
    fun `Test convertRublesToDollars with amount equals 0`() {
        val amount = BigDecimal.valueOf(0.00)

        val expected = BigDecimal.valueOf(0.00)
        val actual = convertRublesToDollars(amount)

        assertEquals(expected, actual)
    }
}