package com.mashjulal.android.financetracker.domain.interactor

import com.mashjulal.android.financetracker.domain.financialcalculations.Account
import com.mashjulal.android.financetracker.domain.financialcalculations.Category
import com.mashjulal.android.financetracker.domain.financialcalculations.OperationType
import com.mashjulal.android.financetracker.domain.repository.AccountRepository
import com.mashjulal.android.financetracker.domain.repository.CategoryRepository
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import javax.inject.Inject

interface GetDataForOptionEditInteractor {
    fun execute(): Single<Pair<List<Account>, Map<OperationType, List<Category>>>>
}

class GetDataForOptionEditInteractorImpl @Inject constructor(
        private val accountRepository: AccountRepository,
        private val categoryRepository: CategoryRepository
) : GetDataForOptionEditInteractor {

    override fun execute(): Single<Pair<List<Account>, Map<OperationType, List<Category>>>> {

        val accounts = accountRepository.getAll()
        val categories = categoryRepository.getAll()
        return Single.zip(accounts, categories, BiFunction { accountList, categoriesList ->
            accountList to categoriesList.groupBy { it.operationType }
        })
    }

}