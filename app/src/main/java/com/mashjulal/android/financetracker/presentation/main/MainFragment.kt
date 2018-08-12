package com.mashjulal.android.financetracker.presentation.main

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.delegateadapter.delegate.diff.DiffUtilCompositeAdapter
import com.example.delegateadapter.delegate.diff.IComparableItem
import com.mashjulal.android.financetracker.App
import com.mashjulal.android.financetracker.R
import com.mashjulal.android.financetracker.domain.financialcalculations.Account
import com.mashjulal.android.financetracker.domain.financialcalculations.OperationType
import com.mashjulal.android.financetracker.presentation.main.recyclerview.balance.BalanceDelegateAdapter
import com.mashjulal.android.financetracker.presentation.main.recyclerview.operation.IncomingsPreviewDelegateAdapter
import com.mashjulal.android.financetracker.presentation.main.recyclerview.operation.IncomingsPreviewViewModel
import com.mashjulal.android.financetracker.presentation.main.recyclerview.operation.OutgoingsPreviewDelegateAdapter
import com.mashjulal.android.financetracker.presentation.main.recyclerview.operation.OutgoingsPreviewViewModel
import com.mashjulal.android.financetracker.presentation.utils.UITextDecorator
import kotlinx.android.synthetic.main.fragment_main.*
import javax.inject.Inject


class MainFragment : MvpAppCompatFragment(), MainPresenter.View {

    private val MAX_ITEM_IN_LIST_COUNT = 5


    companion object {
        const val FRAGMENT_TAG = "MAIN_FRAGMENT_TAG"
        private const val ACCOUNT_PARAM = "accountParam"

        @JvmStatic
        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }

    @Inject
    lateinit var appContext: Context

    @Inject
    @InjectPresenter
    lateinit var presenter: MainPresenter

    @ProvidePresenter
    fun providePresenter() = presenter

    var accountName: String = ""

    private var listener: OnFragmentInteractionListener? = null
    private lateinit var spinnerAccounts: Spinner

    private fun setActionBar() {
        (activity as AppCompatActivity).supportActionBar?.title =
                UITextDecorator.formActionBarTitle(appContext, accountName, true)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_fragment_main, menu)

        val item = menu.findItem(R.id.menuSpinnerAccounts)
        spinnerAccounts = item.actionView as Spinner
        spinnerAccounts.adapter = ArrayAdapter<String>(context,
                R.layout.item_spinner_toolbar, mutableListOf())
        spinnerAccounts.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parentView: AdapterView<*>?) {}

            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?,
                                        position: Int, id: Long) {
                val accountTitle = if (position > 0)
                    UITextDecorator.mapUsableToSpecial(activity?.applicationContext, spinnerAccounts.adapter.getItem(position) as String)
                else ""
//                refreshDataCards(accountTitle)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            accountName = it.getString(ACCOUNT_PARAM)
        }

    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
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
        App.appComponent.inject(this)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw ClassCastException(context.toString() + " must implement OnFragmentInteractionListener")
        }

    }

    override fun onStart() {
        super.onStart()
        activity?.invalidateOptionsMenu()
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
        setActionBar()

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
        fabBalance.setOnClickListener {
            callAddOperation(OperationType.OUTGOINGS)
        }

    }

    private fun callAddOperation(operationType: OperationType) {
        if (accountName.isEmpty()) {
            Toast.makeText(activity?.applicationContext,
                    R.string.error_operation_cant_be_added_to_all, Toast.LENGTH_SHORT).show()
            return
        }
        presenter.requestAddOperation(operationType.toString())
    }

    override fun refreshData(accountTitle: String, data: List<IComparableItem>) {
        accountName = accountTitle
        setActionBar()
        val balance = data[0]
        val incomings = data[1] as IncomingsPreviewViewModel
        val incOps = incomings.operations.map {
            it.copy(category = it.category.copy(title = UITextDecorator.mapSpecialToUsable(appContext, it.category.title)))
        }
        val outgoings = data[2] as OutgoingsPreviewViewModel
        val outOps = outgoings.operations.map {
            it.copy(category = it.category.copy(title = UITextDecorator.mapSpecialToUsable(appContext, it.category.title)))
        }


        val newIncomings = IncomingsPreviewViewModel(incomings.balance,
                incOps.subList(0, Math.min(MAX_ITEM_IN_LIST_COUNT, incOps.size)),
                incOps.size > MAX_ITEM_IN_LIST_COUNT)
        val newOutgoings = OutgoingsPreviewViewModel(outgoings.balance,
                outOps.subList(0, Math.min(MAX_ITEM_IN_LIST_COUNT, outOps.size)),
                outOps.size > MAX_ITEM_IN_LIST_COUNT)

        val adapter = rvMenu.adapter as DiffUtilCompositeAdapter
        adapter.swapData(listOf(balance, newIncomings, newOutgoings))
    }

    override fun setAccounts(data: List<Account>) {
        val entries = listOf(getString(R.string.all_accounts)) +
                data.map { UITextDecorator.mapSpecialToUsable(activity?.applicationContext, it.title) }
        val adapter = ArrayAdapter<String>(context,
                R.layout.item_spinner_toolbar, entries)
        spinnerAccounts.adapter = adapter
        spinnerAccounts.gravity = Gravity.END
        spinnerAccounts.setSelection(0)
    }


    interface OnFragmentInteractionListener {
        fun onAddOperationClicked(operationType: OperationType, accountName: String)
        fun onErrorOccurred(message: String)
    }
}
