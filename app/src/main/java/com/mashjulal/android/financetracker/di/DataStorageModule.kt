package com.mashjulal.android.financetracker.di

import com.mashjulal.android.financetracker.data.currencyconvertapi.CurrencyService
import com.mashjulal.android.financetracker.domain.repository.*
import com.mashjulal.android.financetracker.stub.repository.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataStorageModule {

    @Singleton
    @Provides
    fun providesAccountRepository(): AccountRepository = AccountRepositoryStub()

    @Singleton
    @Provides
    fun providesBalanceRepository(): BalanceRepository = BalanceRepositoryStub()

    @Singleton
    @Provides
    fun providesCurrencyRepository(currencyService: CurrencyService): CurrencyRepository = CurrencyRepositoryStub(currencyService)

    @Singleton
    @Provides
    fun providesOperationRepository(): OperationRepository = OperationRepositoryStub()

    @Singleton
    @Provides
    fun providesCategoryRepository(): CategoryRepository = CategoryRepositoryStub()
}