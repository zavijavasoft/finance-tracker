package com.mashjulal.android.financetracker.domain.repository

import com.mashjulal.android.financetracker.domain.financialcalculations.Account
import io.reactivex.Single

interface AccountRepository {
    fun getById(id: Long): Account?
    fun getAll(): Single<List<Account>>
}