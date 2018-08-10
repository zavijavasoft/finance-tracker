package com.mashjulal.android.financetracker.presentation.chart

import android.content.Context
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.mashjulal.android.financetracker.domain.interactor.RefreshMainScreenDataInteractor
import com.mashjulal.android.financetracker.presentation.main.recyclerview.operation.IncomingsPreviewViewModel
import com.mashjulal.android.financetracker.presentation.main.recyclerview.operation.OutgoingsPreviewViewModel
import com.mashjulal.android.financetracker.presentation.utils.UITextDecorator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

@InjectViewState
class ChartPresenter @Inject constructor(private val appContext: Context, private val refreshInteractor: RefreshMainScreenDataInteractor)
    : MvpPresenter<ChartPresenter.View>() {


    fun needUpdate() {
        refreshInteractor.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { data ->

                    val entries = ArrayList<PieEntry>()

                    val balance = data[0]
                    val incomings = data[1] as IncomingsPreviewViewModel
                    val incOps = incomings.operations.groupBy { it.category.title }
                            .map {
                                var f = 0.0f
                                for (i in it.value)
                                    f += i.amount.amount.toFloat()
                                val usable = UITextDecorator.mapSpecialToUsable(appContext, it.key)
                                entries.add(PieEntry(f, it.key))
                            }
                    val outgoings = data[2] as OutgoingsPreviewViewModel
                    val outOps = outgoings.operations.groupBy { it.category.title }
                            .map {
                                var f = 0.0f
                                for (i in it.value)
                                    f += i.amount.amount.toFloat()
                                val usable = UITextDecorator.mapSpecialToUsable(appContext, it.key)
                                entries.add(PieEntry(f, it.key))

                            }

                    val dataSetIn = PieDataSet(entries, "Диаграмма доходов/расходов")
                    viewState.setData(dataSetIn)
                }
    }


    interface View : MvpView {
        fun setData(incoming: PieDataSet)
    }
}