package com.mashjulal.android.financetracker.presentation.accounts


import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.delegateadapter.delegate.CompositeDelegateAdapter
import com.mashjulal.android.financetracker.App
import com.mashjulal.android.financetracker.R
import com.mashjulal.android.financetracker.domain.financialcalculations.Account
import com.mashjulal.android.financetracker.domain.financialcalculations.Currency
import com.mashjulal.android.financetracker.presentation.utils.UITextDecorator
import kotlinx.android.synthetic.main.fragment_account.*
import javax.inject.Inject

class AccountFragment : MvpAppCompatFragment(), AccountPresenter.View {


    companion object {

        const val FRAGMENT_TAG = "ACCOUNTS_FRAGMENT_TAG"

        @JvmStatic
        fun newInstance() = AccountFragment()

    }


    @Inject
    lateinit var appContext: Context

    @Inject
    @InjectPresenter
    lateinit var presenter: AccountPresenter

    @ProvidePresenter
    fun providePresenter() = presenter


    override fun onAttach(context: Context?) {
        App.appComponent.inject(this)
        super.onAttach(context)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    fun setActionBar() {
        (activity as AppCompatActivity).supportActionBar?.title = appContext.getString(R.string.accounts)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setActionBar()

        val adapter = CompositeDelegateAdapter.Builder<AccountViewModel>()
                .add(AccountDelegateAdapter())
                .build()
        rvAccounts.adapter = adapter
        presenter.needUpdate()

        fabAccounts.setOnClickListener {
            presenter.requestAddAccount()
        }


    }

    override fun update(accounts: List<Account>) {
        val adapter = rvAccounts.adapter as CompositeDelegateAdapter<AccountViewModel>
        val data = accounts.map {
            val usable = UITextDecorator.mapSpecialToUsable(appContext, it.title)
            it.copy(title = usable)
        }.map { AccountViewModel(it) }
        adapter.swapData(data)
    }

    override fun updateCurrencyList(curencies: List<Currency>) {}
}
