package com.mashjulal.android.financetracker.recyclerview

import android.view.View
import com.example.delegateadapter.delegate.KDelegateAdapter
import com.example.delegateadapter.delegate.diff.DiffUtilCompositeAdapter
import com.mashjulal.android.financetracker.R
import com.mashjulal.android.financetracker.formatCurrency
import kotlinx.android.synthetic.main.item_menu_operation_preview.*

sealed class OperationPreviewDelegateAdapter(
        private val title: String,
        private val onClickListener: View.OnClickListener
) : KDelegateAdapter<OperationPreviewViewModel>() {

    override fun getLayoutId(): Int = R.layout.item_menu_operation_preview

    abstract override fun isForViewType(p0: MutableList<*>, p1: Int): Boolean

    override fun onBind(item: OperationPreviewViewModel, viewHolder: KViewHolder) =
            with(viewHolder) {
                tvOperationTitle.text = title
                val adapter = DiffUtilCompositeAdapter.Builder()
                        .add(OperationDetailsDelegateAdapter())
                        .build()
                rvOperations.adapter = adapter
                adapter.swapData(item.operations.map { OperationDetailsViewModel(it) })
                btnShowMore.visibility = if (item.hasMore) View.VISIBLE else View.GONE
                btnNewOperation.setOnClickListener(onClickListener)
                tvTotal.text = formatCurrency(item.balance, item.currency.symbol)
            }
}

class IncomingsPreviewDelegateAdapter(
        title: String,
        onClickListener: View.OnClickListener
) : OperationPreviewDelegateAdapter(title, onClickListener) {
    override fun isForViewType(p0: MutableList<*>, p1: Int): Boolean =
            p0[p1] is IncomingsPreviewViewModel

}

class OutgoingsPreviewDelegateAdapter(
        title: String,
        onClickListener: View.OnClickListener
) : OperationPreviewDelegateAdapter(title, onClickListener) {
    override fun isForViewType(p0: MutableList<*>, p1: Int): Boolean =
            p0[p1] is OutgoingsPreviewViewModel


}

