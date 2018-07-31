package com.mashjulal.android.financetracker.di

import com.mashjulal.android.financetracker.data.currencyconvertapi.CurrencyService
import com.mashjulal.android.financetracker.domain.repository.AccountRepository
import com.mashjulal.android.financetracker.domain.repository.BalanceRepository
import com.mashjulal.android.financetracker.domain.repository.CurrencyRepository
import com.mashjulal.android.financetracker.domain.repository.OperationRepository
import com.mashjulal.android.financetracker.stub.repository.AccountRepositoryStub
import com.mashjulal.android.financetracker.stub.repository.BalanceRepositoryStub
import com.mashjulal.android.financetracker.stub.repository.CurrencyRepositoryStub
import com.mashjulal.android.financetracker.stub.repository.OperationRepositoryStub
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
}