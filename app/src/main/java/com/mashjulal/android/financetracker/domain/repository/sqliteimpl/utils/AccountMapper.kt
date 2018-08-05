package com.mashjulal.android.financetracker.domain.repository.sqliteimpl.utils

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

        fun newAccount(innerAccount: InnerAccount): Account {
            return Account(innerAccount.title(),
                    Money(BigDecimal(innerAccount.sum()),
                            Currency(innerAccount.currency())),
                    Date(innerAccount.lastUpdated()))
        }
    }
}