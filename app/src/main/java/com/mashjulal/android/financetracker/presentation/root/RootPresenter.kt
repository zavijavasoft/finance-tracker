package com.mashjulal.android.financetracker.presentation.root

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import com.mashjulal.android.financetracker.domain.financialcalculations.Account
import com.mashjulal.android.financetracker.domain.financialcalculations.OperationType
import com.mashjulal.android.financetracker.domain.interactor.AddOperationInteractor
import com.mashjulal.android.financetracker.domain.interactor.DispatchAccountInteractor
import com.mashjulal.android.financetracker.domain.interactor.StorageConsistencyInteractor
import com.mashjulal.android.financetracker.route.MainRouter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@InjectViewState
class RootPresenter @Inject constructor(
        val router: MainRouter,
        private val addOperationInteractor: AddOperationInteractor,
        private val storageConsistencyInteractor: StorageConsistencyInteractor,
        private val dispatchAccountInteractor: DispatchAccountInteractor)
    : MvpPresenter<RootPresenter.RootView>() {

    var currentAccoutTitle: String = ""


    enum class MenuItemClass {
        ACCOUNT,
        ACCOUNTS_LIST,
        CATEGORIES_LIST,
        ACCOUNT_CATEGORIES_LIST,
        SETTINGS,
        ABOUT
    }

    val subscription = router.bus
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                val cmd = it
                when (it.command) {
                    MainRouter.SHUTDOWN -> viewState.defaultBackPressed()
                    MainRouter.ACCOUNT_REPLACED -> handleAccountReplaced(it)

                    MainRouter.TO_SINGLE_BALANCE -> viewState.navigateBalance(currentAccoutTitle)
                    MainRouter.TO_SINGLE_ACCOUNT_LIST -> viewState.navigateAccountsList()
                    MainRouter.TO_SINGLE_CATEGORY_LIST -> viewState.navigateCategoriesList()
                    MainRouter.TO_SINGLE_ADD_OPERATION -> viewState.navigateAddOperation(currentAccoutTitle,
                            OperationType.fromString(cmd.param1))
                    MainRouter.TO_SINGLE_ABOUT -> viewState.navigateAbout()
                    MainRouter.TO_SINGLE_SETTINGS -> viewState.navigateSettings()
                    MainRouter.TO_SINGLE_CHART -> viewState.navigateBalance(currentAccoutTitle)
                    MainRouter.TO_SINGLE_ADD_ACCOUNT -> viewState.navigateAddAccount()
                    MainRouter.TO_SINGLE_ADD_CATEGORY -> viewState.navigateAddCategory()
                    MainRouter.TO_TWIN_BALANCE -> {
                        viewState.navigateBalance(currentAccoutTitle)
                        viewState.navigateChart(true)
                    }
                    MainRouter.TO_TWIN_ACCOUNTS_CATEGORIES -> {
                        viewState.navigateAccountsList()
                        viewState.navigateCategoriesList(true)
                    }
                    MainRouter.TO_TWIN_ADD_OPERATION -> {
                        viewState.navigateBalance(currentAccoutTitle)
                        viewState.navigateAddOperation(currentAccoutTitle, OperationType.fromString(cmd.param1), true)
                    }
                    MainRouter.TO_TWIN_ADD_ACCOUNT -> {
                        viewState.navigateAccountsList()
                        viewState.navigateAddAccount(true)

                    }
                    MainRouter.TO_TWIN_ADD_CATEGORY -> {
                        viewState.navigateAddCategory(true)
                        viewState.navigateCategoriesList()

                    }


                }
            }

    fun handleAccountReplaced(it: MainRouter.Command) {
        currentAccoutTitle = it.param1
        dispatchAccountInteractor.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { accountList: List<Account> ->
                    viewState.updateNavigationMenu(accountList)

                }
    }


    fun needUpdate() {
        dispatchAccountInteractor.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { accountList: List<Account> ->
                    viewState.updateNavigationMenu(accountList)
                }
    }

    fun menuItemSelected(itemClass: MenuItemClass, param: String) {
        when (itemClass) {
            MenuItemClass.ABOUT -> router.navigate(MainRouter.Command(MainRouter.TO_SINGLE_ABOUT))
            MenuItemClass.ACCOUNTS_LIST -> router.navigate(MainRouter.Command(MainRouter.TO_SINGLE_ACCOUNT_LIST))
            MenuItemClass.CATEGORIES_LIST -> router.navigate(MainRouter.Command(MainRouter.TO_SINGLE_CATEGORY_LIST))
            MenuItemClass.ACCOUNT_CATEGORIES_LIST -> router.navigate(MainRouter.Command(MainRouter.TO_TWIN_ACCOUNTS_CATEGORIES))
            MenuItemClass.ACCOUNT -> {
                currentAccoutTitle = param
                router.navigate(MainRouter.Command(MainRouter.TO_SINGLE_BALANCE, param))
            }
            MenuItemClass.SETTINGS -> router.navigate(MainRouter.Command(MainRouter.TO_SINGLE_SETTINGS))
        }
    }

    fun backPressed() {
        router.navigate(MainRouter.Command(MainRouter.MOVE_BACK))
    }

    fun notifyOrientation(orientation: Int) {
        router.notifyOrientation(orientation)
    }

    fun executePlanned() {
        addOperationInteractor.executePlanned()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.d("Записи корректны", "")
                }, { e ->
                    Log.d("Ошибка уникальности", e.localizedMessage, e)
                })
    }

    fun initialCheck() {
        storageConsistencyInteractor.check()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.d("Записи корректны", "")
                }, { e ->
                    Log.d("Ошибка уникальности", e.localizedMessage, e)
                })
    }


    fun initialNavigation() {
        viewState.navigateBalance("")
        router.navigate(MainRouter.Command(MainRouter.TO_SINGLE_BALANCE, ""))
    }

    interface RootView : MvpView {
        fun navigateAddOperation(account: String, operationType: OperationType = OperationType.OUTGOINGS, toSecondary: Boolean = false)
        fun navigateBalance(account: String)
        fun navigateSettings()
        fun navigateAbout(toSecondary: Boolean = false)
        fun navigateAccountsList()
        fun navigateAddAccount(toSecondary: Boolean = false)
        fun navigateCategoriesList(toSecondary: Boolean = false)
        fun navigateAddCategory(toSecondary: Boolean = false)
        fun updateNavigationMenu(accounts: List<Account>)
        fun defaultBackPressed()
        fun navigateChart(toSecondary: Boolean)
    }
}