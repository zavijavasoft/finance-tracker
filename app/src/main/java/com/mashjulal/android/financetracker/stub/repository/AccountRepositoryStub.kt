package com.mashjulal.android.financetracker.stub.repository

import com.mashjulal.android.financetracker.domain.financialcalculations.Account
import com.mashjulal.android.financetracker.domain.financialcalculations.Money
import com.mashjulal.android.financetracker.domain.repository.AccountRepository
import io.reactivex.Completable
import io.reactivex.Single

class AccountRepositoryStub : AccountRepository {

    private val data: HashMap<Long, Account> = hashMapOf(
            1L to Account("John Smith"),
            2L to Account("John Smith Credit Card")
    )

    override fun getAll(): Single<List<Account>> {
        return Single.fromCallable { data.map { it.value } }
    }

    override fun getByName(title: String): Single<Account> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update(title: String, money: Money): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}