package com.mashjulal.android.financetracker.presentation.accounts

import com.example.delegateadapter.delegate.KDelegateAdapter
import com.example.delegateadapter.delegate.diff.IComparableItem
import com.mashjulal.android.financetracker.R
import com.mashjulal.android.financetracker.domain.financialcalculations.Account
import com.mashjulal.android.financetracker.formatCurrency
import kotlinx.android.synthetic.main.item_account_details.*


class AccountViewModel(val account: Account) : IComparableItem {

    override fun id(): Any = account.title
    override fun content(): Any = "${account.title} (${account.money.currency.rate})"
}


class AccountDelegateAdapter
    : KDelegateAdapter<AccountViewModel>() {

    override fun getLayoutId(): Int = R.layout.item_account_details

    override fun isForViewType(p0: MutableList<*>, p1: Int): Boolean = p0[p1] is AccountViewModel

    override fun onBind(item: AccountViewModel, viewHolder: KViewHolder) = with(viewHolder) {
        //        ivAccountIcom.setImageResource(item.account.imageRes)
        tvAccount.text = item.account.title
        tvAccountAmount.text = formatCurrency(item.account.money)
    }
}




