package com.mashjulal.android.financetracker.di

import com.mashjulal.android.financetracker.presentation.editoperation.EditOperationActivity
import com.mashjulal.android.financetracker.presentation.main.MainFragment
import dagger.Component
import javax.inject.Singleton

@Component(modules = [DataStorageModule::class, NetworkModule::class, InteractorModule::class])
@Singleton
interface AppComponent {
    fun inject(fragment: MainFragment)
    fun inject(activity: EditOperationActivity)
}