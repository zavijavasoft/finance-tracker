package com.mashjulal.android.financetracker.presentation.root

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import com.mashjulal.android.financetracker.domain.financialcalculations.Account
import com.mashjulal.android.financetracker.domain.financialcalculations.OperationType
import com.mashjulal.android.financetracker.domain.interactor.DispatchAccountInteractor
import com.mashjulal.android.financetracker.route.MainRouter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@InjectViewState
class RootPresenter @Inject constructor(
        val router: MainRouter,
        val dispatchAccountInteractor: DispatchAccountInteractor)
    : MvpPresenter<RootPresenter.RootView>() {

    var currentAccoutTitle: String = ""

    enum class MenuItemClass {
        ACCOUNT,
        ACCOUNTS_LIST,
        CATEGORIES_LIST,
        SETTINGS,
        ABOUT
    }

    val subscription = router.bus
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                when (it.command) {
                    MainRouter.SHUTDOWN -> viewState.defaultBackPressed()
                    MainRouter.CANCEL_OPERATION -> viewState.navigateBalance(currentAccoutTitle)
                    MainRouter.ACCEPT_OPERATION -> viewState.navigateBalance(currentAccoutTitle)
                    MainRouter.REQUEST_ADD_INCOMING_OPERATION -> {
                        viewState.navigateAddOperation(currentAccoutTitle, OperationType.INCOMINGS)
                    }
                    MainRouter.REQUEST_ADD_OUTGOING_OPERATION -> {
                        viewState.navigateAddOperation(currentAccoutTitle, OperationType.OUTGOINGS)
                    }
                    MainRouter.REQUEST_ADD_ACCOUNT -> {
                        viewState.navigateAddAccount()
                    }
                    MainRouter.REQUEST_ADD_CATEGORY -> {
                        viewState.navigateAddCategory()
                    }
                    MainRouter.CATEGORY_REPLACED -> {
                        viewState.navigateCategoriesList()
                    }
                    MainRouter.ACCOUNT_REPLACED -> {
                        currentAccoutTitle = it.param1
                        dispatchAccountInteractor.execute()
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe { accountList: List<Account> ->
                                    viewState.updateNavigationMenu(accountList)
                                    viewState.navigateBalance(currentAccoutTitle)
                                    router.navigate(MainRouter.Command(MainRouter.BALANCE_ACCOUNT_CHANGED, currentAccoutTitle))

                                }
                    }
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
        currentAccoutTitle = ""
        when (itemClass) {
            MenuItemClass.ABOUT -> viewState.navigateAbout()
            MenuItemClass.ACCOUNTS_LIST -> viewState.navigateAccountsList()
            MenuItemClass.CATEGORIES_LIST -> viewState.navigateCategoriesList()
            MenuItemClass.ACCOUNT -> {
                viewState.navigateBalance(param)
                router.navigate(MainRouter.Command(MainRouter.BALANCE_ACCOUNT_CHANGED, param))
                currentAccoutTitle = param
            }

        }
    }

    fun backPressed() {
        router.navigate(MainRouter.Command(MainRouter.BACK_PRESSED))
    }

    interface RootView : MvpView {
        fun navigateAddOperation(account: String, operationType: OperationType = OperationType.OUTGOINGS)
        fun navigateBalance(account: String)
        fun navigateSettings()
        fun navigateAbout()
        fun navigateAccountsList()
        fun navigateAddAccount()
        fun navigateCategoriesList()
        fun navigateAddCategory()
        fun updateNavigationMenu(accounts: List<Account>)
        fun defaultBackPressed()
    }
}