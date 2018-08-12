package com.mashjulal.android.financetracker.presentation.categories

import android.view.View
import com.example.delegateadapter.delegate.KDelegateAdapter
import com.example.delegateadapter.delegate.diff.IComparableItem
import com.mashjulal.android.financetracker.R
import com.mashjulal.android.financetracker.domain.financialcalculations.Category
import com.mashjulal.android.financetracker.domain.financialcalculations.OperationType
import kotlinx.android.synthetic.main.item_category_details.*


class CategoryViewModel(val category: Category) : IComparableItem {

    override fun id(): Any = category.title
    override fun content(): Any = "${category.title} (${if (category.operationType == OperationType.INCOMINGS) "(+)" else "(-)"})"
}


class CategoryDelegateAdapter
    : KDelegateAdapter<CategoryViewModel>() {

    override fun getLayoutId(): Int = R.layout.item_category_details

    override fun isForViewType(p0: MutableList<*>, p1: Int): Boolean = p0[p1] is CategoryViewModel

    override fun onBind(item: CategoryViewModel, viewHolder: KViewHolder) = with(viewHolder) {
        ivCategoryIconInList.setImageResource(item.category.imageRes)
        tvCategoryInList.text = item.category.title
        tvIncomingsTypeInList.visibility =
                if (item.category.operationType == OperationType.INCOMINGS) View.VISIBLE else View.GONE
        tvOutgoingsTypeInList.visibility =
                if (item.category.operationType == OperationType.OUTGOINGS) View.VISIBLE else View.GONE

    }
}
