package com.mashjulal.android.financetracker.financialcalculations

import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Test class for Calculations functions.
 */
class CalculationsTest {

    @Test
    fun `Test convertRublesToDollars with amount above 0`() {
        val amount = BigDecimal.valueOf(111).setScale(2, RoundingMode.HALF_EVEN)

        val expected = BigDecimal.valueOf(1.75).setScale(2, RoundingMode.HALF_EVEN)
        val actual = convertRublesToDollars(amount)

        assertEquals(expected, actual)
    }

    @Test
    fun `Test convertRublesToDollars with amount below 0`() {
        val amount = BigDecimal.valueOf(-111).setScale(2, RoundingMode.HALF_EVEN)

        val expected = BigDecimal.valueOf(-1.75).setScale(2, RoundingMode.HALF_EVEN)
        val actual = convertRublesToDollars(amount)

        assertEquals(expected, actual)
    }

    @Test
    fun `Test convertRublesToDollars with amount equals 0`() {
        val amount = BigDecimal.valueOf(0).setScale(2, RoundingMode.HALF_EVEN)

        val expected = BigDecimal.valueOf(0).setScale(2, RoundingMode.HALF_EVEN)
        val actual = convertRublesToDollars(amount)

        assertEquals(expected, actual)
    }

    @Test
    fun `Test convertDollarsToRubles with amount above 0`() {
        val amount = BigDecimal.valueOf(111).setScale(2, RoundingMode.HALF_EVEN)

        val expected = BigDecimal.valueOf(7045.17).setScale(2, RoundingMode.HALF_EVEN)
        val actual = convertDollarsToRubles(amount)

        assertEquals(expected, actual)
    }

    @Test
    fun `Test convertDollarsToRubles with amount below 0`() {
        val amount = BigDecimal.valueOf(-111).setScale(2, RoundingMode.HALF_EVEN)

        val expected = BigDecimal.valueOf(-7045.17).setScale(2, RoundingMode.HALF_EVEN)
        val actual = convertDollarsToRubles(amount)

        assertEquals(expected, actual)
    }

    @Test
    fun `Test convertDollarsToRubles with amount equals 0`() {
        val amount = BigDecimal.valueOf(0).setScale(2, RoundingMode.HALF_EVEN)

        val expected = BigDecimal.valueOf(0).setScale(2, RoundingMode.HALF_EVEN)
        val actual = convertDollarsToRubles(amount)

        assertEquals(expected, actual)
    }

    @Test
    fun `Test calculateTotal with only incomings`() {
        val operations: MutableList<Operation> = mutableListOf(
                Operation(OperationType.INCOMINGS, BigDecimal.valueOf(10), Currency.RUBLE),
                Operation(OperationType.INCOMINGS, BigDecimal.valueOf(15), Currency.RUBLE),
                Operation(OperationType.INCOMINGS, BigDecimal.valueOf(20), Currency.RUBLE)
        )

        val expected = BigDecimal.valueOf(45).setScale(2, RoundingMode.HALF_EVEN)
        val actual = calculateTotal(operations)

        assertEquals(expected, actual)
    }

    @Test
    fun `Test calculateTotal with only outgoings`() {
        val operations: MutableList<Operation> = mutableListOf(
                Operation(OperationType.OUTGOINGS, BigDecimal.valueOf(10), Currency.RUBLE),
                Operation(OperationType.OUTGOINGS, BigDecimal.valueOf(15), Currency.RUBLE),
                Operation(OperationType.OUTGOINGS, BigDecimal.valueOf(20), Currency.RUBLE)
        )

        val expected = BigDecimal.valueOf(-45).setScale(2, RoundingMode.HALF_EVEN)
        val actual = calculateTotal(operations)

        assertEquals(expected, actual)
    }

    @Test
    fun `Test calculateTotal with only different operations`() {
        val operations: MutableList<Operation> = mutableListOf(
                Operation(OperationType.OUTGOINGS, BigDecimal.valueOf(10), Currency.RUBLE),
                Operation(OperationType.INCOMINGS, BigDecimal.valueOf(15), Currency.RUBLE),
                Operation(OperationType.OUTGOINGS, BigDecimal.valueOf(20), Currency.RUBLE)
        )

        val expected = BigDecimal.valueOf(-15).setScale(2, RoundingMode.HALF_EVEN)
        val actual = calculateTotal(operations)

        assertEquals(expected, actual)
    }

    @Test
    fun `Test calculateTotal with only rubles`() {
        val operations: MutableList<Operation> = mutableListOf(
                Operation(OperationType.OUTGOINGS, BigDecimal.valueOf(10), Currency.RUBLE),
                Operation(OperationType.OUTGOINGS, BigDecimal.valueOf(15), Currency.RUBLE),
                Operation(OperationType.OUTGOINGS, BigDecimal.valueOf(20), Currency.RUBLE)
        )

        val expected = BigDecimal.valueOf(-45).setScale(2, RoundingMode.HALF_EVEN)
        val actual = calculateTotal(operations)

        assertEquals(expected, actual)
    }

    @Test
    fun `Test calculateTotal with only dollars`() {
        val operations: MutableList<Operation> = mutableListOf(
                Operation(OperationType.INCOMINGS, BigDecimal.valueOf(10), Currency.DOLLAR),
                Operation(OperationType.INCOMINGS, BigDecimal.valueOf(20), Currency.DOLLAR),
                Operation(OperationType.INCOMINGS, BigDecimal.valueOf(30), Currency.DOLLAR)
        )

        val expected = BigDecimal.valueOf(3808.2).setScale(2, RoundingMode.HALF_EVEN)
        val actual = calculateTotal(operations)

        assertEquals(expected, actual)
    }

    @Test
    fun `Test calculateTotal with different currencies`() {
        val operations: MutableList<Operation> = mutableListOf(
                Operation(OperationType.OUTGOINGS, BigDecimal.valueOf(10), Currency.RUBLE),
                Operation(OperationType.OUTGOINGS, BigDecimal.valueOf(15), Currency.DOLLAR),
                Operation(OperationType.OUTGOINGS, BigDecimal.valueOf(20), Currency.RUBLE)
        )

        val expected = BigDecimal.valueOf(-982.05).setScale(2, RoundingMode.HALF_EVEN)
        val actual = calculateTotal(operations)

        assertEquals(expected, actual)
    }
}