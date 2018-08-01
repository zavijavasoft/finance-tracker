package com.mashjulal.android.financetracker.presentation.editoperation

import com.mashjulal.android.financetracker.domain.financialcalculations.Account
import com.mashjulal.android.financetracker.domain.financialcalculations.Category
import com.mashjulal.android.financetracker.domain.financialcalculations.Operation
import com.mashjulal.android.financetracker.domain.financialcalculations.OperationType
import com.mashjulal.android.financetracker.domain.interactor.AddOperationInteractor
import com.mashjulal.android.financetracker.domain.interactor.GetDataForOptionEditInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class EditOperationPresenter @Inject constructor(
        private val operationInteractor: AddOperationInteractor,
        private val getDataForOptionEditInteractor: GetDataForOptionEditInteractor
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

    fun requestData() {
        getDataForOptionEditInteractor.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { data ->
                    view?.setData(data.first, data.second)
                }
    }

    interface View {
        fun closeEditWindow()
        fun setData(accounts: List<Account>, categories: Map<OperationType, List<Category>>)
    }
}