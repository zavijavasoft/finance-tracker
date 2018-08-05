package com.mashjulal.android.financetracker.domain.repository.sqliteimpl

import com.mashjulal.android.financetracker.domain.financialcalculations.*
import com.mashjulal.android.financetracker.domain.financialcalculations.Currency
import com.mashjulal.android.financetracker.domain.repository.OperationRepository
import com.mashjulal.android.financetracker.domain.repository.sqliteimpl.model.InnerOperation
import com.mashjulal.android.financetracker.domain.repository.sqliteimpl.utils.AccountMapper
import com.mashjulal.android.financetracker.domain.repository.sqliteimpl.utils.CategoryMapper
import com.mashjulal.android.financetracker.domain.repository.sqliteimpl.utils.OperationMapper
import com.mashjulal.android.financetracker.domain.repository.sqlmodel.OperationModel
import com.squareup.sqldelight.SqlDelightQuery
import io.reactivex.Completable
import io.reactivex.Single
import java.math.BigDecimal
import java.util.*
import javax.inject.Inject

class OperationRepositoryImpl @Inject constructor(private val core: SQLiteCore)
    : OperationRepository {
    override fun getById(id: Long): Single<Operation> {
        val statement: SqlDelightQuery = InnerOperation.FACTORY.SelectOperationById(id)
        return core.database.createQuery(OperationModel.TABLE_NAME, statement.sql)
                .mapToOne { it -> InnerOperation.SELECT_OPERATION_BY_ID.map(it) }
                .map { it: InnerOperation.Companion.JointByIdInfo ->
                    val account = AccountMapper.newAccount(it.A())
                    val category = CategoryMapper.newCategory(it.C())
                    Operation(
                            id,
                            Money(BigDecimal(it.O().sum()), Currency(it.O().currency())),
                            category,
                            Date(it.O().dt()),
                            account
                    )
                }
                .single(Operation())
    }

    override fun getAll(): Single<List<Operation>> {
        val statement: SqlDelightQuery = InnerOperation.FACTORY.SelectAll()
        return core.database.createQuery(OperationModel.TABLE_NAME, statement.sql)
                .mapToList { it -> InnerOperation.ALL_OPERATIONS_MAPPER.map(it) }
                .map { it ->
                    it.map { OperationMapper.newOperation(it.O(), it.C(), it.A()) }
                }
                .take(1).single(listOf())
    }

    override fun getByCategory(category: Category): Single<List<Operation>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

    }

    override fun getAfter(date: Date): Single<List<Operation>> {
        val statement: SqlDelightQuery = InnerOperation.FACTORY.SelectByDateRange(date.time, Date().time)
        return core.database.createQuery(OperationModel.TABLE_NAME, statement.sql)
                .mapToList { it -> InnerOperation.SELECT_OPERATION_BY_ID.map(it) }
                .map { it ->
                    it.map { OperationMapper.newOperation(it.O(), it.C(), it.A()) }
                }
                .take(1).single(listOf())
    }

    override fun getByType(operationType: OperationType): Single<List<Operation>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getByAccountAfter(account: Account, date: Date): Single<List<Operation>> {
        val statement: SqlDelightQuery = InnerOperation.FACTORY.SelectByAccount(account.title)
        return core.database.createQuery(OperationModel.TABLE_NAME, statement.sql)
                .mapToList { it -> InnerOperation.SELECT_OPS_BY_ACCOUNT.map(it) }
                .map { it ->
                    it.map {
                        OperationMapper.newOperation(it.O(), it.C(), it.A())
                    }.filter { it.date.after(date) }
                }
                .take(1).single(listOf())
    }

    override fun insert(operation: Operation): Completable {
        return Completable.fromCallable {
            val db = core.database.writableDatabase
            val insertOperation = OperationModel.InsertOperation(db)
            with(operation) {
                insertOperation.bind(date.time,
                        amount.amount.toDouble(),
                        amount.currency.rate,
                        category.title,
                        account.title,
                        ratio)
            }
            insertOperation.executeInsert()
        }
    }


}

