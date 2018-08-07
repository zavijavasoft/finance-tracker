package com.mashjulal.android.financetracker.presentation.root

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import com.mashjulal.android.financetracker.domain.financialcalculations.Account
import com.mashjulal.android.financetracker.domain.financialcalculations.OperationType
import com.mashjulal.android.financetracker.domain.repository.AccountRepository
import com.mashjulal.android.financetracker.route.MainRouter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@InjectViewState
class RootPresenter @Inject constructor(
        val router: MainRouter,
        val accountRepository: AccountRepository)
    : MvpPresenter<RootPresenter.RootView>() {

    var currentAccoutTitle: String = ""

    enum class MenuItemClass {
        ACCOUNT,
        ALLACCOUNTS,
        SETTINGS,
        ABOUT
    }

    val subscription = router.bus
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                when (it) {
                    MainRouter.CANCEL_OPERATION -> viewState.navigateBalance(currentAccoutTitle)
                    MainRouter.ACCEPT_OPERATION -> viewState.navigateBalance(currentAccoutTitle)
                    MainRouter.REQUEST_ADD_INCOMING_OPERATION -> {
                        viewState.navigateAddOperation(currentAccoutTitle, OperationType.INCOMINGS)
                    }
                    MainRouter.REQUEST_ADD_OUTGOING_OPERATION -> {
                        viewState.navigateAddOperation(currentAccoutTitle, OperationType.OUTGOINGS)
                    }


                }
            }

    init {
    }

    fun needUpdate() {
        accountRepository.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { accountList: List<Account> ->
                    viewState.updateNavigationMenu(accountList)
                }
    }

    fun menuItemSelected(itemClass: MenuItemClass, param: String) {
        currentAccoutTitle = ""
        when (itemClass) {
            MenuItemClass.ALLACCOUNTS -> viewState.navigateBalance("")
            MenuItemClass.ABOUT -> viewState.navigateAbout()
            MenuItemClass.ACCOUNT -> {
                viewState.navigateBalance(param)
                currentAccoutTitle = param
            }
        }
    }

    fun addOperationPressed(operationType: OperationType = OperationType.OUTGOINGS) {

    }

    interface RootView : MvpView {
        fun navigateAddOperation(account: String, operationType: OperationType = OperationType.OUTGOINGS)
        fun navigateBalance(account: String)
        fun navigateSettings()
        fun navigateAbout()
        fun updateNavigationMenu(accounts: List<Account>)
    }
}