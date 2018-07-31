package com.mashjulal.android.financetracker.di

import com.mashjulal.android.financetracker.data.AccountRepositoryImpl
import com.mashjulal.android.financetracker.data.BalanceRepositoryImpl
import com.mashjulal.android.financetracker.data.CurrencyRepositoryImpl
import com.mashjulal.android.financetracker.data.OperationRepositoryImpl
import com.mashjulal.android.financetracker.data.currencyconvertapi.CurrencyService
import com.mashjulal.android.financetracker.domain.repository.AccountRepository
import com.mashjulal.android.financetracker.domain.repository.BalanceRepository
import com.mashjulal.android.financetracker.domain.repository.CurrencyRepository
import com.mashjulal.android.financetracker.domain.repository.OperationRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataStorageModule {

    @Singleton
    @Provides
    fun providesAccountRepository(): AccountRepository = AccountRepositoryImpl()

    @Singleton
    @Provides
    fun providesBalanceRepository(): BalanceRepository = BalanceRepositoryImpl()

    @Singleton
    @Provides
    fun providesCurrencyRepository(currencyService: CurrencyService): CurrencyRepository = CurrencyRepositoryImpl(currencyService)

    @Singleton
    @Provides
    fun providesOperationRepository(): OperationRepository = OperationRepositoryImpl()
}