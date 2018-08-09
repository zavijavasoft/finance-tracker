package com.mashjulal.android.financetracker.di

import com.mashjulal.android.financetracker.App
import com.mashjulal.android.financetracker.data.currencyconvertapi.CurrencyService
import com.mashjulal.android.financetracker.domain.repository.*
import com.mashjulal.android.financetracker.domain.repository.sqliteimpl.*
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class DataStorageModule {

    @Singleton
    @Provides
    fun providesAccountRepository(core: SQLiteCore): AccountRepository = AccountRepositoryImpl(core)

    @Singleton
    @Provides
    fun providesBalanceRepository(sqliteCore: SQLiteCore): BalanceRepository = BalanceRepositoryImpl(sqliteCore)

    @Singleton
    @Provides
    fun providesCurrencyRepository(currencyService: CurrencyService): CurrencyRepository = CurrencyRepositoryImpl(currencyService)

    @Singleton
    @Provides
    fun providesOperationRepository(sqliteCore: SQLiteCore): OperationRepository = OperationRepositoryImpl(sqliteCore)

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