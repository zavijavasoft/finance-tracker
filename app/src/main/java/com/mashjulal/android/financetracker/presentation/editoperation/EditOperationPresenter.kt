package com.mashjulal.android.financetracker.presentation.editoperation

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import com.mashjulal.android.financetracker.domain.financialcalculations.Account
import com.mashjulal.android.financetracker.domain.financialcalculations.Category
import com.mashjulal.android.financetracker.domain.financialcalculations.Operation
import com.mashjulal.android.financetracker.domain.financialcalculations.OperationType
import com.mashjulal.android.financetracker.domain.interactor.AddOperationInteractor
import com.mashjulal.android.financetracker.domain.interactor.GetDataForOptionEditInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@InjectViewState
class EditOperationPresenter @Inject constructor(
        private val operationInteractor: AddOperationInteractor,
        private val getDataForOptionEditInteractor: GetDataForOptionEditInteractor
) : MvpPresenter<EditOperationPresenter.View>() {


    fun saveOperation(operation: Operation) {
        operationInteractor.execute(operation)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    viewState.closeEditWindow()
                }
    }

    fun requestData() {
        getDataForOptionEditInteractor.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { data ->
                    viewState.setData(data.first, data.second)
                }
    }

    interface View : MvpView {
        fun closeEditWindow()
        fun setData(accounts: List<Account>, categories: Map<OperationType, List<Category>>)
    }
}