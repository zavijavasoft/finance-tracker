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
import com.mashjulal.android.financetracker.App
import com.mashjulal.android.financetracker.R
import com.mashjulal.android.financetracker.domain.financialcalculations.Account
import com.mashjulal.android.financetracker.domain.financialcalculations.OperationType
import com.mashjulal.android.financetracker.presentation.main.recyclerview.balance.BalanceDelegateAdapter
import com.mashjulal.android.financetracker.presentation.main.recyclerview.operation.IncomingsPreviewDelegateAdapter
import com.mashjulal.android.financetracker.presentation.main.recyclerview.operation.OutgoingsPreviewDelegateAdapter
import kotlinx.android.synthetic.main.fragment_main.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass for main page.
 * Represents list of menu items with different finance actions:
 * current balance.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class MainFragment : Fragment(), MainPresenter.View {

    @Inject
    lateinit var presenter: MainPresenter
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var spinnerAccounts: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        App.appComponent.inject(this)
        presenter.attachView(this)

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_fragment_main, menu)

        val item = menu.findItem(R.id.menuSpinnerAccounts)
        spinnerAccounts = item.actionView as Spinner
        spinnerAccounts.adapter = ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_dropdown_item, mutableListOf())
        spinnerAccounts.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parentView: AdapterView<*>?) {}

            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?,
                                        position: Int, id: Long) {
                val accountTitle = if (position > 0) spinnerAccounts.adapter.getItem(position) as String
                else ""
                refreshDataCards(accountTitle)
            }
        }
        presenter.getAccountList()
    }

    private fun refreshDataCards(accountName: String = "") {
        if (accountName.isEmpty()) {
            presenter.refreshData()
        } else {
            presenter.refreshData(accountName)
        }
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
        if (::spinnerAccounts.isInitialized) {
            refreshDataCards(spinnerAccounts.selectedItem as String)
        } else {
            refreshDataCards()
        }
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
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = DiffUtilCompositeAdapter.Builder()
                .add(BalanceDelegateAdapter())
                .add(IncomingsPreviewDelegateAdapter(getString(R.string.incomings),
                        View.OnClickListener {
                            callAddOperation(OperationType.INCOMINGS)
                        }))
                .add(OutgoingsPreviewDelegateAdapter(getString(R.string.outgoings),
                        View.OnClickListener {
                            callAddOperation(OperationType.OUTGOINGS)
                        }))
                .build()
        rvMenu.adapter = adapter
    }

    private fun callAddOperation(operationType: OperationType) {
        if (spinnerAccounts.selectedItemPosition == 0) {
            listener?.onErrorOccurred(getString(R.string.error_operation_cant_be_added_to_all))
            return
        }
        val accountName = spinnerAccounts.selectedItem as String
        listener?.onAddOperationClicked(operationType, accountName)
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
        spinnerAccounts.setSelection(0)
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
        fun onAddOperationClicked(operationType: OperationType, accountName: String)
        fun onErrorOccurred(message: String)
    }
}
