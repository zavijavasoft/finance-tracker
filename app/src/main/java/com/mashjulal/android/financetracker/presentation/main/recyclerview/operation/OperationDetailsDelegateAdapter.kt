package com.mashjulal.android.financetracker.presentation.main.recyclerview.operation

import com.example.delegateadapter.delegate.KDelegateAdapter
import com.mashjulal.android.financetracker.R
import com.mashjulal.android.financetracker.formatCurrency
import kotlinx.android.synthetic.main.item_operation_details.*


class OperationDetailsDelegateAdapter
    : KDelegateAdapter<OperationDetailsViewModel>() {

    override fun getLayoutId(): Int = R.layout.item_operation_details

    override fun isForViewType(p0: MutableList<*>, p1: Int): Boolean = p0[p1] is OperationDetailsViewModel

    override fun onBind(item: OperationDetailsViewModel, viewHolder: KViewHolder) = with(viewHolder) {
        ivCategoryIcon.setImageResource(item.operation.category.imageRes)
        tvCategory.text = item.operation.category.title
        tvTotal.text = formatCurrency(item.operation.amount)
    }
}