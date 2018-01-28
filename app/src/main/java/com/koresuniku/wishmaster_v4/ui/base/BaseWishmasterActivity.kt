package com.koresuniku.wishmaster_v4.ui.base

import android.content.Intent
import android.os.Bundle
import android.support.annotation.LayoutRes
import com.koresuniku.wishmaster_v4.R
import com.koresuniku.wishmaster_v4.application.WishmasterApplication
import com.koresuniku.wishmaster_v4.core.base.IActivityMvpView

/**
 * Created by koresuniku on 12.01.18.
 */

abstract class BaseWishmasterActivity : BaseDrawerActivity(), IActivityMvpView {

    protected var isActivityDestroyed = false

    @LayoutRes override abstract fun provideContentLayoutResource(): Int

    override fun getWishmasterApplication(): WishmasterApplication = application as WishmasterApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isActivityDestroyed = false
    }

    override fun finish() {
        super.finish()
        overridePendingTransitionExit()
    }

    override fun startActivity(intent: Intent?) {
        super.startActivity(intent)
        overridePendingTransitionEnter()
    }

    protected fun overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
    }

    protected fun overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_in_back, R.anim.slide_out_back)
    }

    override fun onDestroy() {
        super.onDestroy()
        isActivityDestroyed = true
    }
}