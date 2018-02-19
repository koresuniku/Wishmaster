package com.koresuniku.wishmaster.ui.settings

import android.app.Activity
import android.preference.PreferenceFragment
import android.os.Bundle
import com.koresuniku.wishmaster.R
import android.preference.Preference

/**
 * Created by koresuniku on 2/19/18.
 */

class MainPreferenceFragment : PreferenceFragment(), Preference.OnPreferenceClickListener {

    private lateinit var mCallback: Callback


    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        mCallback = activity as Callback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addPreferencesFromResource(R.xml.main_preference_screen)

        findPreference(getString(R.string.licences_key)).onPreferenceClickListener = this
    }

    override fun onPreferenceClick(preference: Preference): Boolean {
        mCallback.onNestedPreferenceSelected(preference.key)
        return false
    }

    interface Callback {
        fun onNestedPreferenceSelected(key: String)
    }
}