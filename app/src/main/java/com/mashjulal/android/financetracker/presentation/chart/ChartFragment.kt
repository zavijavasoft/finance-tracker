package com.mashjulal.android.financetracker.presentation.chart


import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import com.mashjulal.android.financetracker.App
import com.mashjulal.android.financetracker.R
import kotlinx.android.synthetic.main.fragment_chart.*
import java.util.*
import javax.inject.Inject


class ChartFragment : MvpAppCompatFragment(), ChartPresenter.View {

    companion object {

        const val FRAGMENT_TAG = "CHART_FRAGMENT_TAG"

        @JvmStatic
        fun newInstance() =
                ChartFragment()
    }

    @Inject
    lateinit var appContext: Context

    @Inject
    @InjectPresenter
    lateinit var presenter: ChartPresenter

    @ProvidePresenter
    fun providePresenter() = presenter


    override fun onAttach(context: Context?) {
        App.appComponent.inject(this)
        super.onAttach(context)
    }


    override fun onResume() {
        super.onResume()
        presenter.needUpdate()

    }

    private fun generateCenterSpannableText(): SpannableString {

        val s = SpannableString("MPAndroidChart\ndeveloped by Philipp Jahoda")
        s.setSpan(RelativeSizeSpan(1.7f), 0, 14, 0)
        s.setSpan(StyleSpan(Typeface.NORMAL), 14, s.length - 15, 0)
        s.setSpan(ForegroundColorSpan(Color.GRAY), 14, s.length - 15, 0)
        s.setSpan(RelativeSizeSpan(.8f), 14, s.length - 15, 0)
        s.setSpan(StyleSpan(Typeface.ITALIC), s.length - 14, s.length, 0)
        s.setSpan(ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length - 14, s.length, 0)
        return s
    }


    fun createChart(mChart: PieChart) {
        mChart.setUsePercentValues(true)
        mChart.description.isEnabled = false
        mChart.setExtraOffsets(5f, 10f, 5f, 5f)

        mChart.dragDecelerationFrictionCoef = 0.95f

        //mChart.setCenterTextTypeface(mTfLight)
        mChart.centerText = generateCenterSpannableText()

        mChart.isDrawHoleEnabled = true
        mChart.setHoleColor(Color.WHITE)

        mChart.setTransparentCircleColor(Color.WHITE)
        mChart.setTransparentCircleAlpha(110)

        mChart.holeRadius = 58f
        mChart.transparentCircleRadius = 61f

        mChart.setDrawCenterText(true)

        mChart.rotationAngle = 0f
        // enable rotation of the chart by touch
        mChart.isRotationEnabled = true
        mChart.isHighlightPerTapEnabled = true

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
        //mChart.setOnChartValueSelectedListener(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chart, container, false)
    }


    override fun setData(incoming: PieDataSet) {
        //val mTfRegular = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf")
        //val mTfLight = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf")


        incoming.sliceSpace = 3f
        incoming.iconsOffset = MPPointF(0f, 40f)
        incoming.selectionShift = 5f

        val colors = ArrayList<Int>()

        for (c in ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c)

        for (c in ColorTemplate.JOYFUL_COLORS)
            colors.add(c)

        for (c in ColorTemplate.COLORFUL_COLORS)
            colors.add(c)

        for (c in ColorTemplate.LIBERTY_COLORS)
            colors.add(c)

        for (c in ColorTemplate.PASTEL_COLORS)
            colors.add(c)

        colors.add(ColorTemplate.getHoloBlue())

        incoming.colors = colors
        //dataSet.setSelectionShift(0f);

        val dataIncoming = PieData(incoming)
        dataIncoming.setValueFormatter(PercentFormatter())
        dataIncoming.setValueTextSize(11f)
        dataIncoming.setValueTextColor(Color.WHITE)
        // dataIncoming.setValueTypeface(mTfLight)
        chartIncomings.data = dataIncoming

        // undo all highlights
        chartIncomings.highlightValues(null)

        chartIncomings.invalidate()
    }
}
