package com.koresuniku.wishmaster_v4.ui.base

import android.content.Intent
import android.os.Bundle
import android.support.annotation.LayoutRes
import com.koresuniku.wishmaster_v4.R
import com.koresuniku.wishmaster_v4.application.WishmasterDaggerApplication
import com.koresuniku.wishmaster_v4.core.base.mvp.IMvpPresenter
import com.koresuniku.wishmaster_v4.core.base.mvp.IMvpView

/**
 * Created by koresuniku on 12.01.18.
 */

abstract class BaseWishmasterActivity<P : IMvpPresenter<*>> : BaseDrawerActivity(), IWishamsterActivity, IMvpView<P> {

    protected var isActivityDestroyed = false


    @LayoutRes
    abstract override fun provideContentLayoutResource(): Int

    override fun getWishmasterApplication(): WishmasterDaggerApplication = application as WishmasterDaggerApplication

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