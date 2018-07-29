package com.mashjulal.android.financetracker

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.delegateadapter.delegate.diff.DiffUtilCompositeAdapter
import com.mashjulal.android.financetracker.financialcalculations.Currency
import com.mashjulal.android.financetracker.financialcalculations.Operation
import com.mashjulal.android.financetracker.financialcalculations.OperationType
import com.mashjulal.android.financetracker.recyclerview.*
import kotlinx.android.synthetic.main.fragment_main.*
import java.math.BigDecimal

// TODO: remove hardcode
private val RUBLES = BigDecimal(1000.1)

/**
 * A simple [Fragment] subclass for main page.
 * Represents list of menu items with different finance actions:
 * current balance.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class MainFragment : Fragment() {

    private var listener: OnFragmentInteractionListener? = null
    private val entries = listOf(
            BalanceViewModel(RUBLES),
            IncomingsPreviewViewModel(RUBLES, Currency.RUBLE, listOf(
                    Operation(OperationType.INCOMINGS, BigDecimal.valueOf(100), Currency.RUBLE),
                    Operation(OperationType.INCOMINGS, BigDecimal.valueOf(100), Currency.RUBLE),
                    Operation(OperationType.INCOMINGS, BigDecimal.valueOf(100), Currency.RUBLE)
            ), false),
            OutgoingsPreviewViewModel(BigDecimal.valueOf(2000), Currency.RUBLE, listOf(
                    Operation(OperationType.OUTGOINGS, BigDecimal.valueOf(133), Currency.RUBLE),
                    Operation(OperationType.OUTGOINGS, BigDecimal.valueOf(4324), Currency.RUBLE),
                    Operation(OperationType.OUTGOINGS, BigDecimal.valueOf(1321), Currency.RUBLE)
            ), true)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupActionBar()
    }

    private fun setupActionBar() {
        (activity as AppCompatActivity).supportActionBar?.title =
                getString(R.string.title_activity_main)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw ClassCastException(context.toString() + " must implement OnFragmentInteractionListener")
        }
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
        adapter.swapData(entries)
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
