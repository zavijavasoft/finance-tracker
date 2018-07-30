package com.mashjulal.android.financetracker.domain.repository

import com.mashjulal.android.financetracker.domain.financialcalculations.Account

interface AccountRepository {
    fun getById(id: Long): Account?
    fun getAll(): List<Account>
}