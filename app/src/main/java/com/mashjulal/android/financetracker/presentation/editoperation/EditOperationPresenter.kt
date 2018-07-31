package com.mashjulal.android.financetracker.presentation.editoperation

import com.mashjulal.android.financetracker.domain.financialcalculations.Operation
import com.mashjulal.android.financetracker.domain.interactor.AddOperationInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class EditOperationPresenter @Inject constructor(
        var interactor: AddOperationInteractor
) {

    private var view: View? = null

    fun attachView(view: View) {
        this.view = view
    }

    fun detachView() {
        this.view = null
    }

    fun saveOperation(operation: Operation) {
        interactor.execute(operation)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    view?.closeEditWindow()
                }
    }

    interface View {
        fun closeEditWindow()
    }
}