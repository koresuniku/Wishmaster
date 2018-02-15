package com.koresuniku.wishmaster.ui.base

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.ActivityOptionsCompat
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.application.WishmasterApplication
import com.koresuniku.wishmaster.core.base.mvp.IMvpPresenter
import com.koresuniku.wishmaster.core.base.mvp.IMvpView

/**
 * Created by koresuniku on 12.01.18.
 */

abstract class BaseWishmasterActivity<P : IMvpPresenter<*>> : BaseDrawerActivity(), IWishamsterActivity, IMvpView<P> {

    protected var isActivityDestroyed = false
    protected var isActivityReentered = false

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
    fun launchNextActivityWithtransition(intent: Intent) {
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
}