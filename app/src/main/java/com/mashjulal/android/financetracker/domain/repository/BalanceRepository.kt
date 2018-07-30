package com.mashjulal.android.financetracker.domain.repository

import com.mashjulal.android.financetracker.domain.financialcalculations.Account
import com.mashjulal.android.financetracker.domain.financialcalculations.Balance
import java.util.*

interface BalanceRepository {
    fun getByAccount(account: Account): List<Balance>
    fun getByAccountAfter(account: Account, date: Date): List<Balance>
    fun getLastByAccount(account: Account): Balance
    fun getLastByAll(): List<Balance>
}