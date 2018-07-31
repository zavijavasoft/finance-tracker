package com.mashjulal.android.financetracker.di

import com.mashjulal.android.financetracker.domain.interactor.*
import com.mashjulal.android.financetracker.domain.repository.AccountRepository
import com.mashjulal.android.financetracker.domain.repository.BalanceRepository
import com.mashjulal.android.financetracker.domain.repository.CurrencyRepository
import com.mashjulal.android.financetracker.domain.repository.OperationRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class InteractorModule {

    @Singleton
    @Provides
    fun providesAddOperationInteractor(operationRepository: OperationRepository)
            : AddOperationInteractor = AddOperationInteractorImpl(operationRepository)

    @Singleton
    @Provides
    fun providesRefreshMainScreenDataInteractor(balanceRepository: BalanceRepository,
                                                operationRepository: OperationRepository,
                                                currencyRepository: CurrencyRepository)
            : RefreshMainScreenDataInteractor = RefreshMainScreenDataInteractorImpl(
            balanceRepository, operationRepository, currencyRepository)

    @Singleton
    @Provides
    fun providesRequestAccountInteractor(accountRepository: AccountRepository)
            : RequestAccountInteractor =
            RequestAccountInteractorImpl(accountRepository)

}