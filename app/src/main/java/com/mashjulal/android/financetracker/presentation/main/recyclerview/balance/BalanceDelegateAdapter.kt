package com.mashjulal.android.financetracker.presentation.main.recyclerview.balance

import com.example.delegateadapter.delegate.KDelegateAdapter
import com.mashjulal.android.financetracker.R
import com.mashjulal.android.financetracker.formatCurrency
import kotlinx.android.synthetic.main.item_menu_balance.*

class BalanceDelegateAdapter : KDelegateAdapter<BalanceViewModel>() {

    override fun getLayoutId(): Int = R.layout.item_menu_balance

    override fun isForViewType(items: MutableList<*>, position: Int): Boolean =
            items[position] is BalanceViewModel

    override fun onBind(item: BalanceViewModel, viewHolder: KViewHolder) = with(viewHolder) {
        tvBalanceRubles.text =
                formatCurrency(item.balanceInRubles)
        tvBalanceDollars.text =
                formatCurrency(item.balanceInDollars)
    }

}