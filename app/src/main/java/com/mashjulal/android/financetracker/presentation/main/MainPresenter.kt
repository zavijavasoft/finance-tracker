package com.mashjulal.android.financetracker.presentation.main

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.delegateadapter.delegate.diff.IComparableItem
import com.mashjulal.android.financetracker.domain.financialcalculations.Account
import com.mashjulal.android.financetracker.domain.financialcalculations.OperationType
import com.mashjulal.android.financetracker.domain.interactor.RefreshMainScreenDataInteractor
import com.mashjulal.android.financetracker.domain.interactor.RequestAccountInteractor
import com.mashjulal.android.financetracker.domain.interactor.StorageConsistencyInteractor
import com.mashjulal.android.financetracker.route.MainRouter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@InjectViewState
class MainPresenter @Inject constructor(
        private val router: MainRouter,
        private val refreshInteractor: RefreshMainScreenDataInteractor,
        private val accountInteractor: RequestAccountInteractor,
        private val storageConsistencyInteractor: StorageConsistencyInteractor
) : MvpPresenter<MainPresenter.View>() {


    var justStarted = true


    val subscription = router.bus
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                when (it.command) {
                    MainRouter.BALANCE_ACCOUNT_CHANGED -> {
                        refreshData(it.param1)
                    }


                }
            }




    fun initialCheck() {
        if (justStarted) {
            justStarted = false
            storageConsistencyInteractor.check()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        Log.d("Записи корректны", "")
                    }, { e ->
                        Log.d("Ошибка уникальности", e.localizedMessage, e)
                    })
        }
    }

    fun refreshData() {
        refreshInteractor.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { data ->
                    viewState.refreshData("", data)
                }
    }

    fun getAccountList() {
        accountInteractor.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { data ->
                    viewState.setAccounts(data)
                }
    }

    fun refreshData(accountTitle: String) {
        if (accountTitle.isEmpty()) {
            refreshData()
            return
        }
        val account = Account(accountTitle)
        refreshInteractor.execute(account)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { data ->
                    viewState.refreshData(accountTitle, data)
                }
    }

    fun requestAddOperation(operationType: String) {
        val type = OperationType.fromString(operationType)
        if (type == OperationType.INCOMINGS)
            router.navigate(MainRouter.Command(MainRouter.REQUEST_ADD_INCOMING_OPERATION))
        else
            router.navigate(MainRouter.Command(MainRouter.REQUEST_ADD_OUTGOING_OPERATION))
    }

    interface View : MvpView {

        fun refreshData(accountTitle: String, data: List<IComparableItem>)
        @StateStrategyType(SkipStrategy::class)
        fun setAccounts(data: List<Account>)
    }
}