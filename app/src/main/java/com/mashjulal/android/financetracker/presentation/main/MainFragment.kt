package com.mashjulal.android.financetracker.presentation.main

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.delegateadapter.delegate.diff.DiffUtilCompositeAdapter
import com.example.delegateadapter.delegate.diff.IComparableItem
import com.mashjulal.android.financetracker.R
import com.mashjulal.android.financetracker.data.AccountRepositoryImpl
import com.mashjulal.android.financetracker.data.BalanceRepositoryImpl
import com.mashjulal.android.financetracker.data.CurrencyRepositoryImpl
import com.mashjulal.android.financetracker.data.OperationRepositoryImpl
import com.mashjulal.android.financetracker.data.currencyconvertapi.RetrofitHelper
import com.mashjulal.android.financetracker.domain.financialcalculations.Account
import com.mashjulal.android.financetracker.domain.interactor.RefreshMainScreenDataInteractorImpl
import com.mashjulal.android.financetracker.domain.interactor.RequestAccountInteractorImpl
import com.mashjulal.android.financetracker.presentation.main.recyclerview.balance.BalanceDelegateAdapter
import com.mashjulal.android.financetracker.presentation.main.recyclerview.operation.IncomingsPreviewDelegateAdapter
import com.mashjulal.android.financetracker.presentation.main.recyclerview.operation.OutgoingsPreviewDelegateAdapter
import kotlinx.android.synthetic.main.fragment_main.*

/**
 * A simple [Fragment] subclass for main page.
 * Represents list of menu items with different finance actions:
 * current balance.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class MainFragment : Fragment(), MainPresenter.View {

    private lateinit var presenter: MainPresenter
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var spinnerAccounts: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = MainPresenter(
                RefreshMainScreenDataInteractorImpl(
                        BalanceRepositoryImpl(),
                        OperationRepositoryImpl(), CurrencyRepositoryImpl(RetrofitHelper())),
                RequestAccountInteractorImpl(AccountRepositoryImpl()))
        presenter.attachView(this)

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_fragment_main, menu)

        val item = menu.findItem(R.id.menuSpinnerAccounts)
        spinnerAccounts = item.actionView as Spinner
        spinnerAccounts.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parentView: AdapterView<*>?) {}

            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?,
                                        position: Int, id: Long) {
                val accountTitle = spinnerAccounts.adapter.getItem(position) as String
                if (accountTitle == getString(R.string.all_accounts)) {
                    presenter.refreshData()
                } else {
                    presenter.refreshData(accountTitle)
                }
            }
        }
        presenter.getAccountList()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw ClassCastException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.attachView(this)
    }

    override fun onPause() {
        super.onPause()
        presenter.detachView()
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val newIncomingOperationClick = View.OnClickListener {
            listener!!.onAddIncomingsClicked()
        }
        val newOutgoingOperationClick = View.OnClickListener {
            listener!!.onAddOutgoingsClicked()
        }

        val adapter = DiffUtilCompositeAdapter.Builder()
                .add(BalanceDelegateAdapter())
                .add(IncomingsPreviewDelegateAdapter(getString(R.string.incomings), newIncomingOperationClick))
                .add(OutgoingsPreviewDelegateAdapter(getString(R.string.outgoings), newOutgoingOperationClick))
                .build()
        rvMenu.adapter = adapter
    }

    override fun refreshData(data: List<IComparableItem>) {
        val adapter = rvMenu.adapter as DiffUtilCompositeAdapter
        adapter.swapData(data)
    }

    override fun setAccounts(data: List<Account>) {
        val entries = listOf(getString(R.string.all_accounts)) + data.map { it.title }
        val adapter = ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_dropdown_item, entries)
        spinnerAccounts.adapter = adapter
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment MainFragment.
         */
        fun newInstance() = MainFragment()
    }

    interface OnFragmentInteractionListener {
        fun onAddIncomingsClicked()
        fun onAddOutgoingsClicked()
    }
}
