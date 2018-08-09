package com.mashjulal.android.financetracker.presentation.accounts

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import com.mashjulal.android.financetracker.domain.financialcalculations.Account
import com.mashjulal.android.financetracker.domain.financialcalculations.Currency
import com.mashjulal.android.financetracker.domain.interactor.DispatchAccountInteractor
import com.mashjulal.android.financetracker.domain.repository.CurrencyRepository
import com.mashjulal.android.financetracker.route.MainRouter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@InjectViewState
class AccountPresenter @Inject constructor(private val router: MainRouter,
                                           private val dispatchAccountInteractor: DispatchAccountInteractor,
                                           private val currencyRepository: CurrencyRepository)
    : MvpPresenter<AccountPresenter.View>() {

    fun needUpdate() {
        dispatchAccountInteractor.execute().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { it ->
                    viewState.update(it)
                }
    }


    fun needCurrencyList() {
        currencyRepository.getCurrencyList().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { it ->
                    viewState.updateCurrencyList(it)
                }
    }


    fun updateAccount(title: String, currency: String) {
        dispatchAccountInteractor.updateAccount(title, currency).subscribe {
            router.navigate(MainRouter.Command(MainRouter.ACCOUNT_REPLACED, title))
        }
    }

    fun requestAddAccount() {
        router.navigate(MainRouter.Command(MainRouter.REQUEST_ADD_ACCOUNT))
    }


    interface View : MvpView {
        fun update(accounts: List<Account>)
        fun updateCurrencyList(curencies: List<Currency>)
    }
}