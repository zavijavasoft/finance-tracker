package com.mashjulal.android.financetracker

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.delegateadapter.delegate.diff.DiffUtilCompositeAdapter
import com.mashjulal.android.financetracker.financialcalculations.Currency
import com.mashjulal.android.financetracker.financialcalculations.Operation
import com.mashjulal.android.financetracker.financialcalculations.OperationType
import com.mashjulal.android.financetracker.recyclerview.BalanceDelegateAdapter
import com.mashjulal.android.financetracker.recyclerview.BalanceViewModel
import com.mashjulal.android.financetracker.recyclerview.OperationPreviewDelegateAdapter
import com.mashjulal.android.financetracker.recyclerview.OperationPreviewViewModel
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

    private val entries = listOf(
            BalanceViewModel(RUBLES),
            OperationPreviewViewModel("Incomings", RUBLES, Currency.RUBLE, listOf(
                    Operation(OperationType.INCOMINGS, BigDecimal.valueOf(100), Currency.RUBLE),
                    Operation(OperationType.INCOMINGS, BigDecimal.valueOf(100), Currency.RUBLE),
                    Operation(OperationType.INCOMINGS, BigDecimal.valueOf(100), Currency.RUBLE)
            ), false),
            OperationPreviewViewModel("Outgoings", BigDecimal.valueOf(2000), Currency.RUBLE, listOf(
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val incomingsClick = View.OnClickListener {
            Toast.makeText(context, "Stub for incomings screen", Toast.LENGTH_SHORT).show()
        }
        val outgoingsClick = View.OnClickListener {
            Toast.makeText(context, "Stub for outgoings screen", Toast.LENGTH_SHORT).show()
        }

        val adapter = DiffUtilCompositeAdapter.Builder()
                .add(BalanceDelegateAdapter())
                .add(OperationPreviewDelegateAdapter(incomingsClick))
                .add(OperationPreviewDelegateAdapter(outgoingsClick))
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
}
