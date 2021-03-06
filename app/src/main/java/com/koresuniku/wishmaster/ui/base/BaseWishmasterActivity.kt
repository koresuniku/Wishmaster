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

package com.koresuniku.wishmaster.ui.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.view.Menu
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.application.WMApplication
import com.koresuniku.wishmaster.core.base.IMvpView
import com.koresuniku.wishmaster.ui.settings.SettingsActivity

/**
 * Created by koresuniku on 12.01.18.
 */

abstract class BaseWishmasterActivity : BaseDrawerActivity(), IWishamsterActivity, IMvpView {

    protected var isActivityDestroyed = false
    protected var isActivityReentered = false
    protected var activityMenu: Menu? = null
    var onBackPressedListener: OnBackPressedListener? = null

    @LayoutRes abstract override fun provideContentLayoutResource(): Int
    abstract fun provideFromActivityRequestCode(): Int

    override fun getWishmasterApplication(): WMApplication = application as WMApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isActivityDestroyed = false
    }

    override fun onStop() {
        super.onStop()
        isActivityReentered = true
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_OK)
//        if (onBackPressedListener != null) {
//            onBackPressedListener?.let { if (it.doBack()) super.onBackPressed() }
//        } else
        super.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == provideFromActivityRequestCode() && resultCode == Activity.RESULT_OK) {
            isActivityReentered = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isActivityDestroyed = true
    }

    fun overrideForwardPendingTransition() {
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
    }

    fun overrideBackwardPendingTransition() {
        overridePendingTransition(R.anim.slide_in_back, R.anim.slide_out_back)
    }

    protected fun launchSettingsActivity() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
        overrideForwardPendingTransition()
    }
}