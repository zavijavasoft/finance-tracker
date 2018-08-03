package com.mashjulal.android.financetracker.di

import android.content.Context
import com.mashjulal.android.financetracker.App
import com.mashjulal.android.financetracker.presentation.editoperation.EditOperationActivity
import com.mashjulal.android.financetracker.presentation.main.MainFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(modules = [DataStorageModule::class, NetworkModule::class, InteractorModule::class])
@Singleton
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(app: App): Builder

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }


    fun inject(app: App)
    fun inject(fragment: MainFragment)
    fun inject(activity: EditOperationActivity)
}