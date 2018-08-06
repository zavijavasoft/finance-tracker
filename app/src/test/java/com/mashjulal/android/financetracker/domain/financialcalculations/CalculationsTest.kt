package com.mashjulal.android.financetracker.domain.financialcalculations

import io.reactivex.Single
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
        val actual = calculateTotalEx(operations, Currency.RUBLE
        ) { from, to -> Single.just(BigDecimal.ONE) }

        assertEquals(expected, actual.blockingGet())
    }

    @Test
    fun `Test calculateTotal with only outgoings`() {
        val operations: MutableList<Operation> = mutableListOf(
                initOutgoings(BigDecimal.valueOf(10), Currency.RUBLE),
                initOutgoings(BigDecimal.valueOf(15), Currency.RUBLE),
                initOutgoings(BigDecimal.valueOf(20), Currency.RUBLE)
        )

        val expected = Money(BigDecimal.valueOf(-45).asMoney(), Currency.RUBLE)
        val actual = calculateTotalEx(operations, Currency.RUBLE) { from, to -> Single.just(BigDecimal.ONE) }

        assertEquals(expected, actual.blockingGet())
    }

    @Test
    fun `Test calculateTotal with only different operations`() {
        val operations: MutableList<Operation> = mutableListOf(
                initOutgoings(BigDecimal.valueOf(10), Currency.RUBLE),
                initIncomings(BigDecimal.valueOf(15), Currency.RUBLE),
                initOutgoings(BigDecimal.valueOf(20), Currency.RUBLE)
        )

        val expected = Money(BigDecimal.valueOf(-15).asMoney(), Currency.RUBLE)
        val actual = calculateTotalEx(operations, Currency.RUBLE) { from, to -> Single.just(BigDecimal.ONE) }

        assertEquals(expected, actual.blockingGet())
    }

    @Test
    fun `Test calculateTotal with with conversion`() {
        val operations: MutableList<Operation> = mutableListOf(
                initOutgoings(BigDecimal.valueOf(10), Currency.RUBLE),
                initIncomings(BigDecimal.valueOf(15), Currency.RUBLE),
                initOutgoings(BigDecimal.valueOf(20), Currency.RUBLE)
        )

        val expected = Money(BigDecimal.valueOf(-15 * 60).asMoney(), Currency.DOLLAR)
        val actual = calculateTotalEx(operations, Currency.DOLLAR) { from, to -> Single.just(BigDecimal(60)) }

        assertEquals(expected, actual.blockingGet())
    }

    @Test
    fun `Test calculateTotal with with mixed conversion`() {
        val operations: MutableList<Operation> = mutableListOf(
                initOutgoings(BigDecimal.valueOf(10), Currency.DOLLAR),
                initIncomings(BigDecimal.valueOf(15), Currency.DOLLAR),
                initOutgoings(BigDecimal.valueOf(20), Currency.RUBLE)
        )

        val expected = Money(BigDecimal.valueOf(-20 * 5 + 5).asMoney(), Currency.DOLLAR)
        val actual = calculateTotalEx(operations, Currency.DOLLAR) { from, to ->
            if (from == to)
                Single.just(BigDecimal.ONE)
            else
                Single.just(BigDecimal(5))
        }

        assertEquals(expected, actual.blockingGet())
    }


    @Test
    fun `Test calculateTotal with only rubles`() {
        val operations: MutableList<Operation> = mutableListOf(
                initOutgoings(BigDecimal.valueOf(10), Currency.RUBLE),
                initOutgoings(BigDecimal.valueOf(15), Currency.RUBLE),
                initOutgoings(BigDecimal.valueOf(20), Currency.RUBLE)
        )

        val expected = Money(BigDecimal.valueOf(-45).asMoney(), Currency.RUBLE)
        val actual = calculateTotalEx(operations, Currency.RUBLE) { from, to -> Single.just(BigDecimal.ONE) }

        assertEquals(expected, actual.blockingGet())
    }

    @Test
    fun `Test calculateTotal with only dollars`() {
        val operations: MutableList<Operation> = mutableListOf(
                initIncomings(BigDecimal.valueOf(10), Currency.DOLLAR),
                initIncomings(BigDecimal.valueOf(20), Currency.DOLLAR),
                initIncomings(BigDecimal.valueOf(30), Currency.DOLLAR)
        )

        val expected = Money(BigDecimal.valueOf(60).asMoney(), Currency.DOLLAR)
        val actual = calculateTotalEx(operations, Currency.DOLLAR) { from, to -> Single.just(BigDecimal.ONE) }

        assertEquals(expected, actual.blockingGet())
    }

    @Test
    fun `Test calculateTotal with empty list`() {
        val operations: MutableList<Operation> = mutableListOf()

        val expected = Money(BigDecimal.ZERO, Currency.RUBLE)
        val actual = calculateTotalEx(operations, Currency.RUBLE) { from, to -> Single.just(BigDecimal.ONE) }

        assertEquals(expected, actual.blockingGet())
    }

    private fun initOperation(operationType: OperationType, amount: BigDecimal, currency: Currency): Operation {
        val am = Money(amount, currency)
        val c = Category(operationType, "Cat", -1)
        val d = Calendar.getInstance().time
        val ac = Account("Account")

        return Operation(amount = am, category = c, date = d, account = ac)
    }

    private fun initIncomings(amount: BigDecimal, currency: Currency) =
            initOperation(OperationType.INCOMINGS, amount, currency)

    private fun initOutgoings(amount: BigDecimal, currency: Currency) =
            initOperation(OperationType.OUTGOINGS, amount, currency)
}