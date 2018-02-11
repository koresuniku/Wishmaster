package com.koresuniku.wishmaster.ui.base

import android.content.Intent
import android.os.Bundle
import android.support.annotation.LayoutRes
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.application.WishmasterApplication
import com.koresuniku.wishmaster.core.base.mvp.IMvpPresenter
import com.koresuniku.wishmaster.core.base.mvp.IMvpView

/**
 * Created by koresuniku on 12.01.18.
 */

abstract class BaseWishmasterActivity<P : IMvpPresenter<*>> : BaseDrawerActivity(), IWishamsterActivity, IMvpView<P> {

    protected var isActivityDestroyed = false

    @LayoutRes
    abstract override fun provideContentLayoutResource(): Int

    override fun getWishmasterApplication(): WishmasterApplication = application as WishmasterApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isActivityDestroyed = false
    }

    override fun onBackPressed() {
        super.onBackPressed()
        presenter.unbindView()
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
        presenter.unbindView()
        isActivityDestroyed = true
    }
}