package com.koresuniku.wishmaster.ui.base

import android.app.Activity
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
}