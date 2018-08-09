package com.mashjulal.android.financetracker.stub.repository

import com.mashjulal.android.financetracker.R
import com.mashjulal.android.financetracker.domain.financialcalculations.*
import com.mashjulal.android.financetracker.domain.financialcalculations.Currency
import com.mashjulal.android.financetracker.domain.repository.OperationRepository
import io.reactivex.Completable
import io.reactivex.Single
import java.math.BigDecimal
import java.util.*

private val account: Account = Account("John Smith")
private val calendar = Calendar.getInstance()
private val categoryIn = Category(OperationType.INCOMINGS, "Salary", R.drawable.ic_salary_green_24dp)
private val categoryOut = Category(OperationType.OUTGOINGS, "Bills", R.drawable.ic_bills_red_24dp)
private val tomorrow = Date(calendar.timeInMillis + 1000 * 60 * 60 * 24)
private val tomorrow2Days = Date(tomorrow.time + 1000 * 60 * 60 * 24)

class OperationRepositoryStub : OperationRepository {

    private var data: HashMap<Long, Operation> = hashMapOf(
            1L to Operation(tomorrow.time, Money(BigDecimal.valueOf(10),
                    Currency.RUBLE), categoryIn, tomorrow, account),
            2L to Operation(tomorrow2Days.time, Money(BigDecimal.valueOf(20),
                    Currency.RUBLE), categoryOut, tomorrow2Days, account),
            3L to Operation(tomorrow2Days.time, Money(BigDecimal.valueOf(5),
                    Currency.RUBLE), categoryOut, tomorrow2Days, account),
            4L to Operation(tomorrow2Days.time, Money(BigDecimal.valueOf(35),
                    Currency.RUBLE), categoryIn, tomorrow2Days, account)
    )

    override fun getAll(): Single<List<Operation>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getById(id: Long): Single<Operation> {
        return Single.just(data[id])
    }

    override fun getByCategory(category: Category): Single<List<Operation>> {
        return Single.just(data.asSequence().map { it.value }.filter { it.category == category }.toList())
    }

    override fun getAfter(date: Date): Single<List<Operation>> {
        return Single.just(data.asSequence().map { it.value }.filter { it.date.after(date) }.toList())
    }

    override fun getByType(operationType: OperationType): Single<List<Operation>> {
        return Single.just(data.asSequence().map { it.value }.filter { it.category.operationType == operationType }.toList())
    }

    override fun insert(operation: Operation): Completable {

        return Completable.fromCallable {
            val id = data.size.toLong() + 1
            data[id] = operation
            id
        }
    }

    override fun getByAccountAfter(account: Account, date: Date): Single<List<Operation>> {
        return Single.just(data.asSequence()
                .map { it.value }
                .filter { it.account == account && it.date.after(date) }
                .toList())
    }
}