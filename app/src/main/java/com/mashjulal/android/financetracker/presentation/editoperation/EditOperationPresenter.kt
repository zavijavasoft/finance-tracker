package com.mashjulal.android.financetracker.presentation.editoperation

import com.mashjulal.android.financetracker.domain.financialcalculations.Account
import com.mashjulal.android.financetracker.domain.financialcalculations.Operation
import com.mashjulal.android.financetracker.domain.interactor.AddOperationInteractor
import com.mashjulal.android.financetracker.domain.interactor.RequestAccountInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class EditOperationPresenter @Inject constructor(
        var accountInteractor: RequestAccountInteractor,
        var operationInteractor: AddOperationInteractor
) {

    private var view: View? = null

    fun attachView(view: View) {
        this.view = view
    }

    fun detachView() {
        this.view = null
    }

    fun saveOperation(operation: Operation) {
        operationInteractor.execute(operation)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    view?.closeEditWindow()
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

    interface View {
        fun setAccounts(data: List<Account>)
        fun closeEditWindow()
    }
}