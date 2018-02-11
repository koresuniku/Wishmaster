package com.koresuniku.wishmaster.core.base.rx

import com.koresuniku.wishmaster.core.base.mvp.BaseMvpPresenter
import com.koresuniku.wishmaster.core.base.mvp.IMvpView
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