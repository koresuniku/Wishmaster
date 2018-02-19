package com.koresuniku.wishmaster.ui.settings

import android.preference.PreferenceFragment
import android.os.Bundle
import com.koresuniku.wishmaster.R


/**
 * Created by koresuniku on 2/19/18.
 */

class MainPreferenceFragment : PreferenceFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addPreferencesFromResource(R.xml.main_preference_screen)
    }

}