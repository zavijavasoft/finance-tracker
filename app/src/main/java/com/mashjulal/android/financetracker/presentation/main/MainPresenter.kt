package com.mashjulal.android.financetracker.presentation.main

import com.example.delegateadapter.delegate.diff.IComparableItem
import com.mashjulal.android.financetracker.domain.interactor.RefreshMainScreenDataInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainPresenter(
        private val interactor: RefreshMainScreenDataInteractor
) {

    private var view: View? = null

    fun attachView(view: View) {
        this.view = view
    }

    fun detachView() {
        this.view = null
    }

    fun refreshData() {
        interactor.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { data ->
                    view?.refreshData(data)
                }
    }

    interface View {

        fun refreshData(data: List<IComparableItem>)

    }
}