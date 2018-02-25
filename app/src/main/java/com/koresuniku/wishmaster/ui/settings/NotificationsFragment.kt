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

import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment
import com.koresuniku.wishmaster.R

/**
* Created by koresuniku on 2/25/18.
*/

class NotificationsFragment : PreferenceFragment(), Preference.OnPreferenceChangeListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.notifications_screen)

        findPreference(getString(R.string.switch_new_version_notif_key)).onPreferenceChangeListener = this
    }

    override fun onPreferenceChange(preference: Preference?, value: Any?): Boolean {
        when (preference?.key) {
            getString(R.string.switch_new_version_notif_key) -> {
                if (value is Boolean) {
                    findPreference(getString(R.string.new_version_notif_sound_key)).isEnabled = value
                    findPreference(getString(R.string.new_version_notif_vibration_key)).isEnabled = value
                }
            }
        }
        return true
    }
}