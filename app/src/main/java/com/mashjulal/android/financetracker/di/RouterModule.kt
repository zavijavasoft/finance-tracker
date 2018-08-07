package com.mashjulal.android.financetracker.di

import com.mashjulal.android.financetracker.route.MainRouter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RouterModule {

    @Singleton
    @Provides
    fun provideRouter() = MainRouter()
}