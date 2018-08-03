package com.mashjulal.android.financetracker.di

import com.mashjulal.android.financetracker.App
import com.mashjulal.android.financetracker.data.currencyconvertapi.CurrencyService
import com.mashjulal.android.financetracker.domain.repository.*
import com.mashjulal.android.financetracker.domain.repository.sqliteimpl.CategoryRepositoryImpl
import com.mashjulal.android.financetracker.domain.repository.sqliteimpl.SQLiteCore
import com.mashjulal.android.financetracker.stub.repository.AccountRepositoryStub
import com.mashjulal.android.financetracker.stub.repository.BalanceRepositoryStub
import com.mashjulal.android.financetracker.stub.repository.CurrencyRepositoryStub
import com.mashjulal.android.financetracker.stub.repository.OperationRepositoryStub
import dagger.Module
import dagger.Provides
import javax.inject.Named
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
    fun providesCategoryRepository(core: SQLiteCore): CategoryRepository = CategoryRepositoryImpl(core)

    @Singleton
    @Provides
    fun providesDatabase(app: App, @Named("database_name") databaseName: String, @Named("database_version") databaseVersion: Int): SQLiteCore {
        return SQLiteCore(app, databaseName, databaseVersion)
    }

    @Provides
    @Named("database_name")
    fun provideDatabaseName() = "finance_tracker.db"

    @Provides
    @Named("database_version")
    fun provideDatabaseVersion() = 1

}