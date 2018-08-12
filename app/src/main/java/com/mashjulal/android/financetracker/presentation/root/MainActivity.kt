package com.mashjulal.android.financetracker.presentation.root

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.mashjulal.android.financetracker.App
import com.mashjulal.android.financetracker.R
import com.mashjulal.android.financetracker.domain.financialcalculations.Account
import com.mashjulal.android.financetracker.domain.financialcalculations.OperationType
import com.mashjulal.android.financetracker.presentation.accounts.AccountFragment
import com.mashjulal.android.financetracker.presentation.accounts.AddAccountFragment
import com.mashjulal.android.financetracker.presentation.categories.AddCategoryFragment
import com.mashjulal.android.financetracker.presentation.categories.CategoryFragment
import com.mashjulal.android.financetracker.presentation.chart.ChartFragment
import com.mashjulal.android.financetracker.presentation.editoperation.AddOperationFragment
import com.mashjulal.android.financetracker.presentation.main.AboutFragment
import com.mashjulal.android.financetracker.presentation.main.MainFragment
import com.mashjulal.android.financetracker.presentation.settings.SettingsActivity
import com.mashjulal.android.financetracker.presentation.utils.UITextDecorator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import javax.inject.Inject

/**
 * Application main activity.
 * Contains main actions with user finance.
 */
class MainActivity : MvpAppCompatActivity(),
        RootPresenter.RootView,
        NavigationView.OnNavigationItemSelectedListener, MainFragment.OnFragmentInteractionListener {

    private val ACCOUNT_MENU_BASE_INDEX = 0x7f0df000

    @Inject
    @InjectPresenter
    lateinit var presenter: RootPresenter

    @ProvidePresenter
    fun providePresenter() = presenter

    private var usableAccountTitlesSorted = listOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        App.appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


        val toggle = ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)

        val orientation = resources.configuration.orientation
        presenter.notifyOrientation(orientation)
        if (savedInstanceState == null) {
            presenter.initialCheck()
            presenter.executePlanned()
            presenter.initialNavigation()
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.needUpdate()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            // if navigation is visible close it
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            presenter.backPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        var fragment: Fragment? = null
        when (item.itemId) {
            R.id.navItemMain -> {
                presenter.menuItemSelected(RootPresenter.MenuItemClass.ACCOUNT, "")
            }
            R.id.navItemSettings -> {
                // open settings
                startActivity(SettingsActivity.newIntent(this))
            }
            R.id.navItemAccounts -> {
                // open account list
                presenter.menuItemSelected(RootPresenter.MenuItemClass.ACCOUNTS_LIST, "")
            }

            R.id.navItemCategories -> {
                // open account list
                presenter.menuItemSelected(RootPresenter.MenuItemClass.CATEGORIES_LIST, "")
            }

            R.id.navItemAccountsCategories -> {
                presenter.menuItemSelected(RootPresenter.MenuItemClass.ACCOUNT_CATEGORIES_LIST, "")
            }

            R.id.navItemAbout -> {
                // open about page
                presenter.menuItemSelected(RootPresenter.MenuItemClass.ABOUT, "")
            }
            else -> {
                val accountTitle = usableAccountTitlesSorted[item.itemId - ACCOUNT_MENU_BASE_INDEX]
                if (accountTitle != null) {
                    val special = UITextDecorator.mapUsableToSpecial(applicationContext, item.title.toString())
                    presenter.menuItemSelected(RootPresenter.MenuItemClass.ACCOUNT, special)
                }
            }

        }

        if (fragment != null) {
            supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onAddOperationClicked(operationType: OperationType, accountName: String) {
    }

    override fun onErrorOccurred(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    private fun navigateSingleFragment(tag: String, toSecondary: Boolean, newFragment: () -> Fragment) {
        val containerId = if (toSecondary) R.id.container_secondary else R.id.container
            supportFragmentManager.beginTransaction()
                    .replace(containerId, newFragment(), tag)
                    .commit()
    }

    override fun defaultBackPressed() {
        super.onBackPressed()
    }

    override fun navigateAddOperation(account: String, operationType: OperationType, toSecondary: Boolean) {
        navigateSingleFragment(AddOperationFragment.FRAGMENT_TAG, toSecondary)
        { AddOperationFragment.newInstance(account, operationType.toString()) }
    }


    override fun navigateBalance(account: String) {
        navigateSingleFragment(MainFragment.FRAGMENT_TAG, false) { MainFragment.newInstance() }
    }

    override fun navigateSettings() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun navigateAbout(toSecondary: Boolean) {
        navigateSingleFragment(AboutFragment.FRAGMENT_TAG, toSecondary) { AboutFragment.newInstance() }
    }

    override fun updateNavigationMenu(accounts: List<Account>) {
        val submenu = navView.menu.getItem(1).subMenu
        submenu.clear()
        submenu.setIcon(R.drawable.ic_card_giftcard_green_24dp)

        usableAccountTitlesSorted = accounts.asSequence()
                .map { UITextDecorator.mapSpecialToUsable(applicationContext, it.title) }
                .sorted().toList()

        for ((idx, account) in usableAccountTitlesSorted.withIndex()) {
            submenu.add(1, ACCOUNT_MENU_BASE_INDEX + idx, idx, account) // id is idx+ my constant
            val item = submenu.getItem(idx)
            item.setIcon(R.drawable.ic_salary_green_24dp)
        }
    }

    override fun navigateAccountsList() {
        navigateSingleFragment(AccountFragment.FRAGMENT_TAG, false) { AccountFragment.newInstance() }
    }

    override fun navigateAddAccount(toSecondary: Boolean) {
        navigateSingleFragment(AddAccountFragment.FRAGMENT_TAG, toSecondary) { AddAccountFragment.newInstance() }
    }

    override fun navigateCategoriesList(toSecondary: Boolean) {
        navigateSingleFragment(CategoryFragment.FRAGMENT_TAG, toSecondary) { CategoryFragment.newInstance() }
    }

    override fun navigateAddCategory(toSecondary: Boolean) {
        navigateSingleFragment(AddCategoryFragment.FRAGMENT_TAG, toSecondary) { AddCategoryFragment.newInstance() }
    }

    override fun navigateChart(toSecondary: Boolean) {
        navigateSingleFragment(ChartFragment.FRAGMENT_TAG, toSecondary) { ChartFragment.newInstance() }
    }

}
