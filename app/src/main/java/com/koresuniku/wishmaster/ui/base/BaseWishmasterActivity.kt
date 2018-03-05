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

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.ActivityOptionsCompat
import android.view.Menu
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.application.WishmasterApplication
import com.koresuniku.wishmaster.core.base.IMvpPresenter
import com.koresuniku.wishmaster.core.base.IMvpView
import com.koresuniku.wishmaster.ui.settings.SettingsActivity

/**
 * Created by koresuniku on 12.01.18.
 */

abstract class BaseWishmasterActivity<P : IMvpPresenter<*>> :
        BaseDrawerActivity(), IWishamsterActivity, IMvpView<P> {

    protected var isActivityDestroyed = false
    protected var isActivityReentered = false
    protected var activityMenu: Menu? = null

    @LayoutRes abstract override fun provideContentLayoutResource(): Int
    abstract fun provideFromActivityRequestCode(): Int

    override fun getWishmasterApplication(): WishmasterApplication = application as WishmasterApplication

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
        presenter.unbindView()
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
        presenter.unbindView()
        isActivityDestroyed = true
    }

    @SuppressLint("RestrictedApi")
    protected fun launchNextActivityWithtransition(intent: Intent) {
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            startActivityForResult(intent, provideFromActivityRequestCode(), options.toBundle())
        } else startActivityForResult(intent, provideFromActivityRequestCode())
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