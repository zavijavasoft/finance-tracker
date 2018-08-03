package com.mashjulal.android.financetracker.di

import com.mashjulal.android.financetracker.domain.interactor.*
import com.mashjulal.android.financetracker.domain.repository.*
import com.mashjulal.android.financetracker.domain.repository.sqliteimpl.SQLiteCore
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class InteractorModule {

    @Singleton
    @Provides
    fun providesAddOperationInteractor(balanceRepository: BalanceRepository,
                                       currencyRepository: CurrencyRepository,
                                       operationRepository: OperationRepository)
            : AddOperationInteractor = AddOperationInteractorImpl(balanceRepository,
            currencyRepository, operationRepository)

    @Singleton
    @Provides
    fun providesRefreshMainScreenDataInteractor(balanceRepository: BalanceRepository,
                                                operationRepository: OperationRepository)
            : RefreshMainScreenDataInteractor = RefreshMainScreenDataInteractorImpl(
            balanceRepository, operationRepository)

    @Singleton
    @Provides
    fun providesRequestAccountInteractor(accountRepository: AccountRepository)
            : RequestAccountInteractor =
            RequestAccountInteractorImpl(accountRepository)

    @Singleton
    @Provides
    fun providesGetDataForOptionEditInteractor(accountRepository: AccountRepository,
                                               categoryRepository: CategoryRepository)
            : GetDataForOptionEditInteractor =
            GetDataForOptionEditInteractorImpl(accountRepository, categoryRepository)

    @Singleton
    @Provides
    fun prividesStorageConsistencyInteractor(sqlCore: SQLiteCore): StorageConsistencyInteractor =
            StorageConsistanceInteracrorImpl(sqlCore)
}