package com.mashjulal.android.financetracker.presentation.categories


import android.content.Context
import android.os.Bundle
import android.view.*
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.mashjulal.android.financetracker.App
import com.mashjulal.android.financetracker.R
import com.mashjulal.android.financetracker.domain.financialcalculations.Category
import com.mashjulal.android.financetracker.domain.financialcalculations.OperationType
import kotlinx.android.synthetic.main.fragment_add_category.*
import javax.inject.Inject


class AddCategoryFragment : MvpAppCompatFragment(), CategoryPresenter.View {

    companion object {

        const val FRAGMENT_TAG = "ADD_CATEGORY_FRAGMENT_TAG"

        @JvmStatic
        fun newInstance() = AddCategoryFragment()
    }


    @Inject
    lateinit var appContext: Context

    @Inject
    @InjectPresenter
    lateinit var presenter: CategoryPresenter

    @ProvidePresenter
    fun providePresenter() = presenter


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        App.appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.needUpdate()
        setHasOptionsMenu(true)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_activity_edit_operation, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuItemDone -> {
                var imageRes: Int = R.drawable.ic_bills_red_24dp
                val operationTypeSelected = spinnerOperationTypeInNewCategory.selectedItem.toString()
                val operationType = if (operationTypeSelected == appContext.getString(R.string.incomings)) {
                    imageRes = R.drawable.ic_cash_back_green_24dp
                    OperationType.INCOMINGS
                } else
                    OperationType.OUTGOINGS
                presenter.updateCategory(newCategoryTitle.text.toString(),
                        operationType.toString(), imageRes)
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun update(categories: List<Category>) {}

}
