package com.mashjulal.android.financetracker.stub.repository

import com.mashjulal.android.financetracker.domain.financialcalculations.Account
import com.mashjulal.android.financetracker.domain.repository.AccountRepository
import io.reactivex.Single

class AccountRepositoryStub : AccountRepository {

    private val data: HashMap<Long, Account> = hashMapOf(
            1L to Account("John Smith"),
            2L to Account("John Smith Credit Card")
    )

    override fun getById(id: Long): Account? {
        return data[id]
    }

    override fun getAll(): Single<List<Account>> {
        return Single.fromCallable { data.map { it.value } }
    }
}