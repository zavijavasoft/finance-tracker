package com.mashjulal.android.financetracker.stub.repository

import com.mashjulal.android.financetracker.R
import com.mashjulal.android.financetracker.domain.financialcalculations.*
import com.mashjulal.android.financetracker.domain.financialcalculations.Currency
import com.mashjulal.android.financetracker.domain.repository.OperationRepository
import java.math.BigDecimal
import java.util.*

private val account: Account = Account("John Smith")
private val calendar = Calendar.getInstance()
private val category = Category("Salary", R.drawable.ic_github)
private val tomorrow = Date(calendar.timeInMillis + 1000 * 60 * 60 * 24)
private val tomorrow2Days = Date(tomorrow.time + 1000 * 60 * 60 * 24)

class OperationRepositoryStub : OperationRepository {

    private var data: HashMap<Long, Operation> = hashMapOf(
            1L to IncomingsOperation(Money(BigDecimal.valueOf(10),
                    Currency.RUBLE), category, tomorrow, account),
            2L to OutgoingsOperation(Money(BigDecimal.valueOf(20),
                    Currency.RUBLE), category, tomorrow2Days, account),
            3L to OutgoingsOperation(Money(BigDecimal.valueOf(5),
                    Currency.RUBLE), category, tomorrow2Days, account),
            4L to IncomingsOperation(Money(BigDecimal.valueOf(35),
                    Currency.RUBLE), category, tomorrow2Days, account)
    )

    override fun getById(id: Long): Operation? {
        return data[id]
    }

    override fun getByCategory(category: Category): List<Operation> {
        return data.asSequence().map { it.value }.filter { it.category == category }.toList()
    }

    override fun getAfter(date: Date): List<Operation> {
        return data.asSequence().map { it.value }.filter { it.date.after(date) }.toList()
    }

    override fun getByType(operationType: OperationType): List<Operation> {
        return data.asSequence().map { it.value }.filter { it.operationType == operationType }.toList()
    }

    override fun insert(operation: Operation): Long {
        val id = data.size.toLong() + 1
        data[id] = operation
        return id
    }

    override fun getByAccountAfter(account: Account, date: Date): List<Operation> {
        return data.asSequence()
                .map { it.value }
                .filter { it.account == account && it.date.after(date) }
                .toList()
    }
}