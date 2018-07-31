package com.mashjulal.android.financetracker.presentation.settings

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.preference.*
import android.text.TextUtils
import android.view.MenuItem
import com.mashjulal.android.financetracker.R

/**
 * A [PreferenceActivity] that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 *
 */
class SettingsActivity : AppCompatPreferenceActivity() {

    companion object {
        /**
         * A preference value change listener that updates the preference's summary
         * to reflect its new value.
         */
        private val bindPreferenceSummaryToValueListener = Preference.OnPreferenceChangeListener { preference, value ->
            val stringValue = value.toString()

            when (preference) {
                is ListPreference -> setSummaryToListPreference(preference, stringValue)
                is RingtonePreference -> setSummaryToRingtonePreference(preference, stringValue)
                else -> {
                    // For all other preferences, set the summary to the value's
                    // simple string representation.
                    preference.summary = stringValue
                }
            }
            true
        }

        /**
         * Helper method to set summary value of list preference.
         */
        private fun setSummaryToListPreference(
                preference: ListPreference, stringValue: String) {
            val index = preference.findIndexOfValue(stringValue)
            val summary = if (index >= 0) preference.entries[index] else null
            preference.summary = summary
        }

        /**
         * Helper method to set summary value of ringtone preference.
         */
        private fun setSummaryToRingtonePreference(
                preference: RingtonePreference, stringValue: String) {
            if (TextUtils.isEmpty(stringValue)) {
                // Empty values correspond to 'silent' (no ringtone).
                preference.setSummary(R.string.pref_ringtone_silent)
            } else {
                val ringtone = RingtoneManager
                        .getRingtone(preference.context, Uri.parse(stringValue))
                if (ringtone == null) {
                    // Clear the summary if there was a lookup error.
                    preference.summary = null
                } else {
                    val name = ringtone.getTitle(preference.context)
                    preference.summary = name
                }
            }
        }

        /**
         * Helper method to determine if the device has an extra-large screen. For
         * example, 10" tablets are extra-large.
         */
        private fun isXLargeTablet(context: Context): Boolean {
            return context.resources.configuration.screenLayout and
                    Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_XLARGE
        }

        /**
         * Binds a preference's summary to its value. More specifically, when the
         * preference's value is changed, its summary (line of text below the
         * preference title) is updated to reflect the value. The summary is also
         * immediately updated upon calling this method. The exact display format is
         * dependent on the type of preference.

         * @see .bindPreferenceSummaryToValueListener
         */
        private fun bindPreferenceSummaryToValue(preference: Preference) {
            preference.onPreferenceChangeListener = bindPreferenceSummaryToValueListener
            val currentValue = PreferenceManager.getDefaultSharedPreferences(preference.context)
                    .getString(preference.key, "")
            bindPreferenceSummaryToValueListener.onPreferenceChange(preference, currentValue)
        }

        /**
         * Use this method to start [SettingsActivity].
         * @param context component intent
         * @return new intent instance
         */
        fun newIntent(context: Context) = Intent(context, SettingsActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupActionBar()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Set up the [android.app.ActionBar], if the API is available.
     */
    private fun setupActionBar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    /**
     * {@inheritDoc}
     */
    override fun onIsMultiPane(): Boolean {
        return isXLargeTablet(this)
    }

    /**
     * {@inheritDoc}
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    override fun onBuildHeaders(target: List<PreferenceActivity.Header>) {
        loadHeadersFromResource(R.xml.pref_headers, target)
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    override fun isValidFragment(fragmentName: String): Boolean {
        return PreferenceFragment::class.java.name == fragmentName
                || NotificationPreferenceFragment::class.java.name == fragmentName
    }

    /**
     * This fragment shows notification preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    class NotificationPreferenceFragment : PreferenceFragment() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.pref_notification)
            setHasOptionsMenu(true)

            bindPreferenceSummaryToValue(findPreference("notifications_new_message_ringtone"))
        }
    }
}
