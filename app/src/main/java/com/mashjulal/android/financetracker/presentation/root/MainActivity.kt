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
import com.mashjulal.android.financetracker.presentation.editoperation.AddOperationFragment
import com.mashjulal.android.financetracker.presentation.editoperation.EditOperationActivity
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

        if (savedInstanceState == null) {
            val fragment = MainFragment.newInstance("")
            supportFragmentManager.beginTransaction().add(R.id.container, fragment).commit()
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
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        var fragment: Fragment? = null
        when (item.itemId) {
            R.id.navItemMain -> {
                presenter.menuItemSelected(RootPresenter.MenuItemClass.ALLACCOUNTS, item.title.toString())
            }
            R.id.navItemSettings -> {
                // open settings
                startActivity(SettingsActivity.newIntent(this))
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
        startActivity(EditOperationActivity.newIntent(this, operationType, accountName))
    }

    override fun onErrorOccurred(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    private fun navigateSingleFragment(tag: String, newFragment: () -> Fragment) {
        val fragment = supportFragmentManager
                .findFragmentByTag(tag)
        if (fragment == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, newFragment(), tag)
                    .commit()

        }

    }

    override fun navigateAddOperation(account: String, operationType: OperationType) {
        navigateSingleFragment(AddOperationFragment.FRAGMENT_TAG)
        { AddOperationFragment.newInstance(account, operationType.toString()) }
    }


    override fun navigateBalance(account: String) {
        val special = UITextDecorator.mapUsableToSpecial(applicationContext, account)
        navigateSingleFragment(MainFragment.FRAGMENT_TAG) { MainFragment.newInstance(special) }

    }

    override fun navigateSettings() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun navigateAbout() {
        navigateSingleFragment(AboutFragment.FRAGMENT_TAG) { AboutFragment.newInstance() }
    }

    override fun updateNavigationMenu(accounts: List<Account>) {
        val submenu = navView.menu.getItem(1).subMenu
        submenu.clear()
        submenu.setIcon(R.drawable.ic_github)

        usableAccountTitlesSorted = accounts.asSequence()
                .map { UITextDecorator.mapSpecialToUsable(applicationContext, it.title) }
                .sorted().toList()

        for ((idx, account) in usableAccountTitlesSorted.withIndex()) {
            submenu.add(1, ACCOUNT_MENU_BASE_INDEX + idx, idx, account) // id is idx+ my constant
            val item = submenu.getItem(idx)
            item.setIcon(R.drawable.ic_github)
        }


    }
}
