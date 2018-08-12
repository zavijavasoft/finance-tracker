package com.mashjulal.android.financetracker.presentation.accounts


import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.mashjulal.android.financetracker.App
import com.mashjulal.android.financetracker.R
import com.mashjulal.android.financetracker.domain.financialcalculations.Account
import com.mashjulal.android.financetracker.domain.financialcalculations.Currency
import com.mashjulal.android.financetracker.presentation.utils.UITextDecorator
import kotlinx.android.synthetic.main.fragment_add_account.*
import javax.inject.Inject


class AddAccountFragment : MvpAppCompatFragment(), AccountPresenter.View {

    companion object {

        const val FRAGMENT_TAG = "ADD_ACCOUNT_FRAGMENT_TAG"

        @JvmStatic
        fun newInstance() = AddAccountFragment()
    }


    @Inject
    lateinit var appContext: Context

    @Inject
    @InjectPresenter
    lateinit var presenter: AccountPresenter

    @ProvidePresenter
    fun providePresenter() = presenter


    private val forbiddenNames = mutableSetOf<String>()
    private var currencies = listOf<Currency>()

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        App.appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.needUpdate()
        presenter.needCurrencyList()
        setHasOptionsMenu(true)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_activity_edit_operation, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuItemDone -> {
                presenter.updateAccount(newAccountTitle.text.toString(),
                        spinnerCurrencyInNewAccount.selectedItem.toString())
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun update(accounts: List<Account>) {

        forbiddenNames.clear()
        accounts.forEach {
            forbiddenNames.add(it.title)
            forbiddenNames.add(UITextDecorator.mapSpecialToUsable(appContext, it.title))
        }
    }

    override fun updateCurrencyList(curencies: List<Currency>) {
        this.currencies = currencies
        val adapter = ArrayAdapter(appContext,
                android.R.layout.simple_spinner_item,
                curencies.map { UITextDecorator.mapSpecialToUsable(appContext, it.rate) })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCurrencyInNewAccount.adapter = adapter
        spinnerCurrencyInNewAccount.setSelection(0)

    }
}
