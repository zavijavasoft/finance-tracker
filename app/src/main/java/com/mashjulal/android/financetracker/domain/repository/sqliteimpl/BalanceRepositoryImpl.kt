package com.mashjulal.android.financetracker.domain.repository.sqliteimpl

import com.mashjulal.android.financetracker.domain.financialcalculations.Account
import com.mashjulal.android.financetracker.domain.financialcalculations.Balance
import com.mashjulal.android.financetracker.domain.repository.BalanceRepository
import com.mashjulal.android.financetracker.domain.repository.sqliteimpl.model.InnerAccount
import com.mashjulal.android.financetracker.domain.repository.sqliteimpl.utils.AccountMapper
import com.mashjulal.android.financetracker.domain.repository.sqliteimpl.utils.BalanceMapper
import com.mashjulal.android.financetracker.domain.repository.sqlmodel.AccountModel
import com.squareup.sqldelight.SqlDelightQuery
import io.reactivex.Single
import java.util.*
import javax.inject.Inject

class BalanceRepositoryImpl @Inject constructor(val core: SQLiteCore)
    : BalanceRepository {

    override fun getLastByAccount(account: Account): Single<List<Balance>> {
        val statement: SqlDelightQuery = InnerAccount.FACTORY.SelectByAccount(account.title)
        return core.database.createQuery(AccountModel.TABLE_NAME, statement.sql, account.title)
                .mapToList { it -> InnerAccount.SELECT_ACCOUNT_BY_ACCOUNT.map(it) }
                .map { it ->
                    it.map { it ->
                        BalanceMapper.newBalance(AccountMapper.newAccount(it))
                    }.toList()
                }.take(1).single(listOf())
    }

    override fun getLastByAll(): Single<List<Balance>> {
        val statement: SqlDelightQuery = InnerAccount.FACTORY.SelectAll()
        return core.database.createQuery(AccountModel.TABLE_NAME, statement.sql)
                .mapToList { it -> InnerAccount.ALL_ACCOUNTS_MAPPER.map(it) }
                .map { it ->
                    it.map { it ->
                        BalanceMapper.newBalance(AccountMapper.newAccount(it))
                    }.toList()
                }.take(1).single(listOf())
    }

    override fun getByAccount(account: Account): Single<List<Balance>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getByAccountAfter(account: Account, date: Date): Single<List<Balance>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}