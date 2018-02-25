/*
 * Copyright (c) 2018 koresuniku
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

        findPreference(getString(R.string.licenses_key)).onPreferenceClickListener = this
        findPreference(getString(R.string.notifications_key)).onPreferenceClickListener = this
    }

    override fun onPreferenceClick(preference: Preference): Boolean {
        mCallback.onNestedPreferenceSelected(preference.key)
        return false
    }

    interface Callback {
        fun onNestedPreferenceSelected(key: String)
    }
}