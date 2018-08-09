package com.mashjulal.android.financetracker.domain.repository.sqliteimpl

import com.mashjulal.android.financetracker.domain.financialcalculations.Account
import com.mashjulal.android.financetracker.domain.financialcalculations.Money
import com.mashjulal.android.financetracker.domain.repository.AccountRepository
import com.mashjulal.android.financetracker.domain.repository.sqliteimpl.model.InnerAccount
import com.mashjulal.android.financetracker.domain.repository.sqliteimpl.utils.AccountMapper
import com.mashjulal.android.financetracker.domain.repository.sqlmodel.AccountModel
import com.squareup.sqldelight.SqlDelightQuery
import io.reactivex.Completable
import io.reactivex.Single
import java.util.*
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(val core: SQLiteCore) : AccountRepository {


    override fun getByName(title: String): Single<List<Account>> {
        val statement: SqlDelightQuery = InnerAccount.FACTORY.SelectByAccount(title)
        return core.database.createQuery(AccountModel.TABLE_NAME, statement.sql, title)
                .mapToList { it -> InnerAccount.SELECT_ACCOUNT_BY_ACCOUNT.map(it) }
                .map { it ->
                    it.map { it ->
                        AccountMapper.newAccount(it)
                    }.toList()
                }.take(1).single(listOf())
    }


    override fun getAll(): Single<List<Account>> {
        val statement: SqlDelightQuery = InnerAccount.FACTORY.SelectAll()
        return core.database.createQuery(AccountModel.TABLE_NAME, statement.sql)
                .mapToList { it -> InnerAccount.ALL_ACCOUNTS_MAPPER.map(it) }
                .map { it ->
                    it.map { it ->
                        AccountMapper.newAccount(it)
                    }.toList()
                }.take(1).single(listOf())
    }

    override fun update(title: String, money: Money): Completable {
        return Completable.fromCallable {
            val db = core.database.writableDatabase
            val updateAccount = AccountModel.UpdateAccount(db)
            updateAccount.bind(money.amount.toDouble(), Date().time, title)
            updateAccount.executeUpdateDelete()
        }
    }

    override fun insert(title: String, money: Money): Completable {
        return Completable.fromCallable {
            val db = core.database.writableDatabase
            val insertAccount = AccountModel.InsertAccount(db)
            insertAccount.bind(title, money.currency.rate, 0.0, Date().time)
            insertAccount.executeInsert()
        }
    }


}
