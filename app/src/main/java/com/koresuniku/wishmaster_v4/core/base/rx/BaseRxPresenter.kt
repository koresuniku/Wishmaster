package com.koresuniku.wishmaster_v4.core.base.rx

import com.koresuniku.wishmaster_v4.core.base.mvp.BaseMvpPresenter
import com.koresuniku.wishmaster_v4.core.base.mvp.IMvpView
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by koresuniku on 12.11.17.
 */

abstract class BaseRxPresenter<V: IMvpView<*>>(override val compositeDisposable: CompositeDisposable):
        BaseMvpPresenter<V>(), IRxObject {


    override fun unbindView() {
        super.unbindView()
        compositeDisposable.clear()
    }
}