package com.mashjulal.android.financetracker.presentation.main

import com.example.delegateadapter.delegate.diff.IComparableItem
import com.mashjulal.android.financetracker.domain.financialcalculations.Account
import com.mashjulal.android.financetracker.domain.interactor.RefreshMainScreenDataInteractor
import com.mashjulal.android.financetracker.domain.interactor.RequestAccountInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainPresenter(
        private val refreshInteractor: RefreshMainScreenDataInteractor,
        private val accountInteractor: RequestAccountInteractor
) {

    private var view: View? = null

    fun attachView(view: View) {
        this.view = view
    }

    fun detachView() {
        this.view = null
    }

    fun refreshData() {
        refreshInteractor.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { data ->
                    view?.refreshData(data)
                }
    }

    fun getAccountList() {
        accountInteractor.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { data ->
                    view?.setAccounts(data)
                }
    }

    fun refreshData(accountTitle: String) {
        val account = Account(accountTitle)
        refreshInteractor.execute(account)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { data ->
                    view?.refreshData(data)
                }
    }

    interface View {

        fun refreshData(data: List<IComparableItem>)
        fun setAccounts(data: List<Account>)
    }
}