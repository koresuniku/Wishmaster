package com.koresuniku.wishmaster.core.base.rx

import com.koresuniku.wishmaster.core.base.mvp.BaseMvpDataPresenter
import com.koresuniku.wishmaster.core.base.mvp.IMvpView
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by koresuniku on 02.02.18.
 */
abstract class BaseRxDataPresenter<V: IMvpView<*>, D>(override var compositeDisposable: CompositeDisposable):
        BaseMvpDataPresenter<V, D>(), IRxObject {

    override fun unbindView() {
        super.unbindView()
        compositeDisposable.clear()
    }
}