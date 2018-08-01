package com.mashjulal.android.financetracker.presentation.main

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.Toast
import com.mashjulal.android.financetracker.R
import com.mashjulal.android.financetracker.domain.financialcalculations.OperationType
import com.mashjulal.android.financetracker.presentation.editoperation.EditOperationActivity
import com.mashjulal.android.financetracker.presentation.settings.SettingsActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

/**
 * Application main activity.
 * Contains main actions with user finance.
 */
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, MainFragment.OnFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)

        // Show main page
        val fragment = MainFragment.newInstance()
        supportFragmentManager.beginTransaction().add(R.id.container, fragment).commit()
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
                // open main page
                fragment = MainFragment.newInstance()
            }
            R.id.navItemSettings -> {
                // open settings
                startActivity(SettingsActivity.newIntent(this))
            }
            R.id.navItemAbout -> {
                // open about page
                fragment = AboutFragment.newInstance()
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
}
