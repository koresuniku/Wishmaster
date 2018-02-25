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

import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import butterknife.BindView
import butterknife.ButterKnife
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.application.utils.IntentKeystore
import com.koresuniku.wishmaster.core.modules.settings.ISettingsPresenter
import com.koresuniku.wishmaster.ui.base.BaseWishmasterActivity
import javax.inject.Inject

/**
 * Created by koresuniku on 2/19/18.
 */

class SettingsActivity : BaseWishmasterActivity<ISettingsPresenter>(), MainPreferenceFragment.Callback {

    @Inject override lateinit var presenter: ISettingsPresenter

    @BindView(R.id.toolbar) lateinit var mToolbar: Toolbar

    override fun provideContentLayoutResource() = R.layout.activity_settings
    override fun provideFromActivityRequestCode() = IntentKeystore.FROM_SETTINGS_ACTIVITY_REQUEST_CODE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWishmasterApplication().daggerSettingsViewComponent.inject(this)
        ButterKnife.bind(this)

        setupToolbar()

        fragmentManager.beginTransaction()
                .replace(R.id.preference_fragment_container, MainPreferenceFragment(), getString(R.string.settings_text))
                .commit()
    }

    private fun setupToolbar() {
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mToolbar.post { mToolbar.title = getString(R.string.settings_text) }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
         if (supportFragmentManager.fragments.size > 1) {
             mToolbar.title = supportFragmentManager.fragments.last().tag
         } else {
             mToolbar.title = getString(R.string.settings_text)
         }
        super.onBackPressed()
        overrideBackwardPendingTransition()
    }

    override fun onNestedPreferenceSelected(key: String) {
        var title = String()
        when (key) {
            getString(R.string.licenses_key) -> {
                title = getString(R.string.licenses_title)
                addFragment(LicensesFragment(), title)
            }
            getString(R.string.notifications_key) -> {
                title = getString(R.string.notifications_title)
                addFragment(NotificationsFragment(), title)
            }
        }
        mToolbar.title = title
    }

    private fun addFragment(fragment: Fragment, tag: String) {
        fragmentManager.beginTransaction()
                .setCustomAnimations(
                        R.animator.from_right, R.animator.to_left,
                        R.animator.from_left, R.animator.to_right)
                .replace(R.id.preference_fragment_container, fragment, tag)
                .addToBackStack(tag)
                .commit()
    }
}