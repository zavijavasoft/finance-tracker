package com.mashjulal.android.financetracker.financialcalculations

import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal
import java.math.RoundingMode

class OperationTest {

    @Test
    fun `Test field initialization`() {
        val actual = Operation(OperationType.INCOMINGS, BigDecimal.valueOf(15), Currency.RUBLE)

        assertEquals(OperationType.INCOMINGS, actual.operationType)
        assertEquals(BigDecimal.valueOf(15).setScale(2, RoundingMode.HALF_EVEN), actual.amount)
        assertEquals(Currency.RUBLE, actual.currency)
    }

    @Test
    fun `Test amount initializes with negative value`() {
        val actual = Operation(OperationType.INCOMINGS, BigDecimal.valueOf(-15), Currency.RUBLE)

        assertEquals(BigDecimal.valueOf(15).setScale(2, RoundingMode.HALF_EVEN), actual.amount)
    }
}