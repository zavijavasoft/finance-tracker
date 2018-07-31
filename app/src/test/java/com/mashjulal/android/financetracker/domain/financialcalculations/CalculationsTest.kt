package com.mashjulal.android.financetracker.domain.financialcalculations

import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal
import java.util.*

class CalculationsTest {

    @Test
    fun `Test calculateTotal with only incomings`() {
        val operations: MutableList<Operation> = mutableListOf(
                initIncomings(BigDecimal.valueOf(10), Currency.RUBLE),
                initIncomings(BigDecimal.valueOf(15), Currency.RUBLE),
                initIncomings(BigDecimal.valueOf(20), Currency.RUBLE)
        )

        val expected = Money(BigDecimal.valueOf(45).asMoney(), Currency.RUBLE)
        val actual = calculateTotal(operations)

        assertEquals(expected, actual)
    }

    @Test
    fun `Test calculateTotal with only outgoings`() {
        val operations: MutableList<Operation> = mutableListOf(
                initOutgoings(BigDecimal.valueOf(10), Currency.RUBLE),
                initOutgoings(BigDecimal.valueOf(15), Currency.RUBLE),
                initOutgoings(BigDecimal.valueOf(20), Currency.RUBLE)
        )

        val expected = Money(BigDecimal.valueOf(-45).asMoney(), Currency.RUBLE)
        val actual = calculateTotal(operations)

        assertEquals(expected, actual)
    }

    @Test
    fun `Test calculateTotal with only different operations`() {
        val operations: MutableList<Operation> = mutableListOf(
                initOutgoings(BigDecimal.valueOf(10), Currency.RUBLE),
                initIncomings(BigDecimal.valueOf(15), Currency.RUBLE),
                initOutgoings(BigDecimal.valueOf(20), Currency.RUBLE)
        )

        val expected = Money(BigDecimal.valueOf(-15).asMoney(), Currency.RUBLE)
        val actual = calculateTotal(operations)

        assertEquals(expected, actual)
    }

    @Test
    fun `Test calculateTotal with only rubles`() {
        val operations: MutableList<Operation> = mutableListOf(
                initOutgoings(BigDecimal.valueOf(10), Currency.RUBLE),
                initOutgoings(BigDecimal.valueOf(15), Currency.RUBLE),
                initOutgoings(BigDecimal.valueOf(20), Currency.RUBLE)
        )

        val expected = Money(BigDecimal.valueOf(-45).asMoney(), Currency.RUBLE)
        val actual = calculateTotal(operations)

        assertEquals(expected, actual)
    }

    @Test
    fun `Test calculateTotal with only dollars`() {
        val operations: MutableList<Operation> = mutableListOf(
                initIncomings(BigDecimal.valueOf(10), Currency.DOLLAR),
                initIncomings(BigDecimal.valueOf(20), Currency.DOLLAR),
                initIncomings(BigDecimal.valueOf(30), Currency.DOLLAR)
        )

        val expected = Money(BigDecimal.valueOf(60).asMoney(), Currency.DOLLAR)
        val actual = calculateTotal(operations)

        assertEquals(expected, actual)
    }

    @Test
    fun `Test calculateTotal with different currencies`() {
        val operations: MutableList<Operation> = mutableListOf(
                initOutgoings(BigDecimal.valueOf(10), Currency.RUBLE),
                initOutgoings(BigDecimal.valueOf(15), Currency.DOLLAR),
                initOutgoings(BigDecimal.valueOf(20), Currency.RUBLE)
        )

        val expected = Money(BigDecimal.valueOf(-973.87), Currency.RUBLE)
        val actual = calculateTotal(operations, BigDecimal(62.924896))

        assertEquals(expected, actual)
    }

    @Test
    fun `Test calculateTotal with empty list`() {
        val operations: MutableList<Operation> = mutableListOf()

        val expected = Money(BigDecimal.ZERO, Currency.RUBLE)
        val actual = calculateTotal(operations)

        assertEquals(expected, actual)
    }

    private fun initOperation(operationType: OperationType, amount: BigDecimal, currency: Currency): Operation {
        val am = Money(amount, currency)
        val c = Category("Cat", -1)
        val d = Calendar.getInstance().time
        val ac = Account("Account")


        return if (operationType == OperationType.INCOMINGS) IncomingsOperation(am, c, d, ac)
        else OutgoingsOperation(am, c, d, ac)
    }

    private fun initIncomings(amount: BigDecimal, currency: Currency) =
            initOperation(OperationType.INCOMINGS, amount, currency)

    private fun initOutgoings(amount: BigDecimal, currency: Currency) =
            initOperation(OperationType.OUTGOINGS, amount, currency)
}