package com.mashjulal.android.financetracker.di

import android.content.Context
import com.mashjulal.android.financetracker.App
import com.mashjulal.android.financetracker.presentation.accounts.AccountFragment
import com.mashjulal.android.financetracker.presentation.accounts.AddAccountFragment
import com.mashjulal.android.financetracker.presentation.categories.AddCategoryFragment
import com.mashjulal.android.financetracker.presentation.categories.CategoryFragment
import com.mashjulal.android.financetracker.presentation.editoperation.AddOperationFragment
import com.mashjulal.android.financetracker.presentation.main.MainFragment
import com.mashjulal.android.financetracker.presentation.root.MainActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(modules = [DataStorageModule::class, NetworkModule::class, InteractorModule::class, RouterModule::class])
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
    fun inject(fragment: AccountFragment)
    fun inject(fragment: CategoryFragment)
    fun inject(fragment: AddOperationFragment)
    fun inject(fragment: AddAccountFragment)
    fun inject(fragment: AddCategoryFragment)
    fun inject(mainActivity: MainActivity)
}