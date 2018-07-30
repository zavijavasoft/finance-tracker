package com.mashjulal.android.financetracker.presentation.main

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.delegateadapter.delegate.diff.DiffUtilCompositeAdapter
import com.example.delegateadapter.delegate.diff.IComparableItem
import com.mashjulal.android.financetracker.R
import com.mashjulal.android.financetracker.data.BalanceRepositoryImpl
import com.mashjulal.android.financetracker.data.OperationRepositoryImpl
import com.mashjulal.android.financetracker.domain.interactor.RefreshMainScreenDataInteractorImpl
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

    private var presenter: MainPresenter? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = MainPresenter(RefreshMainScreenDataInteractorImpl(
                BalanceRepositoryImpl(), OperationRepositoryImpl()
        ))
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

    override fun onResume() {
        super.onResume()
        presenter?.attachView(this)
        presenter?.refreshData()
    }

    override fun onPause() {
        super.onPause()
        presenter?.detachView()
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
        adapter.notifyDataSetChanged()
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
