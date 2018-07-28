package com.mashjulal.android.financetracker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity


class AboutActivity : AppCompatActivity() {

    companion object {
        /**
         * Use this method to start [SettingsActivity].
         * @param context component intent
         * @return new intent instance
         */
        fun newIntent(context: Context) = Intent(context, AboutActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
    }
}
