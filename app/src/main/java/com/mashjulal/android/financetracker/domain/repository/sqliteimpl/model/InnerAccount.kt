package com.mashjulal.android.financetracker.domain.repository.sqliteimpl.model

import com.google.auto.value.AutoValue
import com.mashjulal.android.financetracker.domain.repository.sqlmodel.AccountModel
import com.squareup.sqldelight.RowMapper

@AutoValue
abstract class InnerAccount : AccountModel {
    companion object {
        val FACTORY: AccountModel.Factory<InnerAccount> =
                AccountModel.Factory(AccountModel.Creator<InnerAccount> { id, title, currency, sum, lastUpdated ->
                    AutoValue_InnerAccount(id, title, currency, sum, lastUpdated)
                })

        val ALL_ACCOUNTS_MAPPER: RowMapper<InnerAccount> = FACTORY.selectAllMapper()
        val SELECT_ACCOUNT_BY_ACCOUNT: RowMapper<InnerAccount> = FACTORY.selectByAccountMapper()
    }

}
