package com.mashjulal.android.financetracker.domain.repository.sqliteimpl.utils

import com.mashjulal.android.financetracker.domain.financialcalculations.Currency
import com.mashjulal.android.financetracker.domain.financialcalculations.Money
import com.mashjulal.android.financetracker.domain.financialcalculations.Operation
import com.mashjulal.android.financetracker.domain.repository.sqliteimpl.model.InnerAccount
import com.mashjulal.android.financetracker.domain.repository.sqliteimpl.model.InnerCategory
import com.mashjulal.android.financetracker.domain.repository.sqliteimpl.model.InnerOperation
import java.math.BigDecimal

class OperationMapper {

    companion object {
        fun newOperation(innerOperation: InnerOperation, innerCategory: InnerCategory, innerAccount: InnerAccount): Operation {
            val account = AccountMapper.newAccount(innerAccount)
            val category = CategoryMapper.newCategory(innerCategory)

            return Operation(
                    innerOperation.id(),
                    Money(BigDecimal(innerOperation.sum()), Currency(innerOperation.currency())),
                    category,
                    java.util.Date(innerOperation.dt()),
                    account
            )

        }
    }

}