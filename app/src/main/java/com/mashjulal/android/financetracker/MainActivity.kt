package com.mashjulal.android.financetracker

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

/**
 * Application main activity.
 * Contains main actions with user finance.
 */
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

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
        supportFragmentManager.beginTransaction().add(R.id.container, MainFragment.newInstance()).commit()
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
        // Handle navigation view item clicks here.
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
}
