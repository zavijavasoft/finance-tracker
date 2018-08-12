package com.mashjulal.android.financetracker.domain.repository.sqliteimpl.utils

import android.support.annotation.DrawableRes
import com.mashjulal.android.financetracker.R
import com.mashjulal.android.financetracker.domain.financialcalculations.Account
import com.mashjulal.android.financetracker.domain.financialcalculations.Balance
import com.mashjulal.android.financetracker.domain.financialcalculations.Currency
import com.mashjulal.android.financetracker.domain.financialcalculations.Money
import com.mashjulal.android.financetracker.domain.repository.sqliteimpl.model.InnerAccount
import java.math.BigDecimal
import java.util.*


// Не стал выносить этот маппер в отдельный файл, потому что само понятие баланса неразрывно
// связано с аккаунтом
class BalanceMapper {
    companion object {
        fun newBalance(account: Account): Balance {
            return Balance(account, account.money, account.lastUpdated)
        }
    }
}

class AccountMapper {

    companion object {


        const val PREDEFINED_ACCOUNT = "Cash(RUB)"


        fun newAccount(innerAccount: InnerAccount): Account {
            return Account(innerAccount.title(),
                    Money(BigDecimal(innerAccount.sum()),
                            Currency(innerAccount.currency())),
                    Date(innerAccount.lastUpdated()))
        }

        val mapSpecialToUsable = hashMapOf(
                PREDEFINED_ACCOUNT to R.string.predefined_account
        )

        val mapUsableToSpecial = hashMapOf(
                R.string.predefined_account to PREDEFINED_ACCOUNT
        )

        fun specialToUsableId(special: String) = mapSpecialToUsable[special]
                ?: R.string.predefined_wrong

        fun usableIdToSpecial(@DrawableRes id: Int) = mapUsableToSpecial[id] ?: PREDEFINED_ACCOUNT

        fun hasItId(@DrawableRes id: Int) = mapUsableToSpecial.keys.contains(id)

        fun isUndeletable(special: String) = special in listOf(PREDEFINED_ACCOUNT)
    }
}