package com.mashjulal.android.financetracker.presentation.categories


import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.delegateadapter.delegate.CompositeDelegateAdapter
import com.mashjulal.android.financetracker.App
import com.mashjulal.android.financetracker.R
import com.mashjulal.android.financetracker.domain.financialcalculations.Category
import com.mashjulal.android.financetracker.presentation.utils.UITextDecorator
import kotlinx.android.synthetic.main.fragment_category.*
import javax.inject.Inject

class CategoryFragment : MvpAppCompatFragment(), CategoryPresenter.View {


    companion object {

        const val FRAGMENT_TAG = "CATEGORIES_FRAGMENT_TAG"

        @JvmStatic
        fun newInstance() = CategoryFragment()

    }


    @Inject
    lateinit var appContext: Context

    @Inject
    @InjectPresenter
    lateinit var presenter: CategoryPresenter

    @ProvidePresenter
    fun providePresenter() = presenter


    override fun onAttach(context: Context?) {
        App.appComponent.inject(this)
        super.onAttach(context)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_category, container, false)
    }

    private fun setActionBar() {
        (activity as AppCompatActivity).supportActionBar?.title = appContext.getString(R.string.categories)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setActionBar()

        val adapter = CompositeDelegateAdapter.Builder<CategoryViewModel>()
                .add(CategoryDelegateAdapter())
                .build()
        rvCategories.adapter = adapter
        presenter.needUpdate()

        fabCategories.setOnClickListener {
            presenter.requestAddCategory()
        }


    }

    override fun update(accounts: List<Category>) {
        val adapter = rvCategories.adapter as CompositeDelegateAdapter<CategoryViewModel>
        val data = accounts.map {
            val usable = UITextDecorator.mapSpecialToUsable(appContext, it.title)
            it.copy(title = usable)
        }.map { CategoryViewModel(it) }
        adapter.swapData(data)
    }

}
