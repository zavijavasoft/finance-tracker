package com.mashjulal.android.financetracker.financialcalculations

import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal

class CalculationsTest {

    @Test
    fun `Test convertRublesToDollars with amount above 0`() {
        val amount = BigDecimal.valueOf(111).asMoney()

        val expected = BigDecimal.valueOf(1.75).asMoney()
        val actual = convertRublesToDollars(amount)

        assertEquals(expected, actual)
    }

    @Test
    fun `Test convertRublesToDollars with amount below 0`() {
        val amount = BigDecimal.valueOf(-111).asMoney()

        val expected = BigDecimal.valueOf(-1.75).asMoney()
        val actual = convertRublesToDollars(amount)

        assertEquals(expected, actual)
    }

    @Test
    fun `Test convertRublesToDollars with amount equals 0`() {
        val amount = BigDecimal.valueOf(0).asMoney()

        val expected = BigDecimal.valueOf(0).asMoney()
        val actual = convertRublesToDollars(amount)

        assertEquals(expected, actual)
    }

    @Test
    fun `Test convertDollarsToRubles with amount above 0`() {
        val amount = BigDecimal.valueOf(111).asMoney()

        val expected = BigDecimal.valueOf(7045.17).asMoney()
        val actual = convertDollarsToRubles(amount)

        assertEquals(expected, actual)
    }

    @Test
    fun `Test convertDollarsToRubles with amount below 0`() {
        val amount = BigDecimal.valueOf(-111).asMoney()

        val expected = BigDecimal.valueOf(-7045.17).asMoney()
        val actual = convertDollarsToRubles(amount)

        assertEquals(expected, actual)
    }

    @Test
    fun `Test convertDollarsToRubles with amount equals 0`() {
        val amount = BigDecimal.valueOf(0).asMoney()

        val expected = BigDecimal.valueOf(0).asMoney()
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

        val expected = BigDecimal.valueOf(45).asMoney()
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

        val expected = BigDecimal.valueOf(-45).asMoney()
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

        val expected = BigDecimal.valueOf(-15).asMoney()
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

        val expected = BigDecimal.valueOf(-45).asMoney()
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

        val expected = BigDecimal.valueOf(3808.2).asMoney()
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

        val expected = BigDecimal.valueOf(-982.05).asMoney()
        val actual = calculateTotal(operations)

        assertEquals(expected, actual)
    }
}