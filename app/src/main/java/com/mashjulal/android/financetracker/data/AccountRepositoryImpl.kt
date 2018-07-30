package com.mashjulal.android.financetracker.data

import com.mashjulal.android.financetracker.domain.financialcalculations.Account
import com.mashjulal.android.financetracker.domain.repository.AccountRepository

class AccountRepositoryImpl : AccountRepository {

    private val data: HashMap<Long, Account> = hashMapOf(
            1L to Account("John Smith")
    )

    override fun getById(id: Long): Account? {
        return data[id]
    }

    override fun getAll(): List<Account> {
        return data.map { it.value }
    }
}