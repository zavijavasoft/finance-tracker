package com.mashjulal.android.financetracker.presentation.categories

import android.support.annotation.DrawableRes
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import com.mashjulal.android.financetracker.domain.financialcalculations.Category
import com.mashjulal.android.financetracker.domain.interactor.DispatchCategoryInteractor
import com.mashjulal.android.financetracker.route.MainRouter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@InjectViewState
class CategoryPresenter @Inject constructor(private val router: MainRouter,
                                            private val dispatchCategoryInteractor: DispatchCategoryInteractor)
    : MvpPresenter<CategoryPresenter.View>() {

    fun needUpdate() {
        dispatchCategoryInteractor.execute().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { it ->
                    viewState.update(it)
                }
    }


    fun updateCategory(title: String, operationType: String, @DrawableRes iconId: Int) {
        dispatchCategoryInteractor.updateCategory(title, operationType, iconId).subscribe {
            router.navigate(MainRouter.Command(MainRouter.CATEGORY_REPLACED))
        }
    }

    fun requestAddCategory() {
        router.navigate(MainRouter.Command(MainRouter.REQUEST_ADD_CATEGORY))
    }


    interface View : MvpView {
        fun update(categories: List<Category>)
    }
}