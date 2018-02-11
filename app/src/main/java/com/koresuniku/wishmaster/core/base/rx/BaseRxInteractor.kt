package com.koresuniku.wishmaster.core.base.rx

import com.koresuniku.wishmaster.core.base.mvp.BaseMvpInteractor
import com.koresuniku.wishmaster.core.base.mvp.IMvpPresenter
import io.reactivex.disposables.CompositeDisposable


abstract class BaseRxInteractor<P : IMvpPresenter<*>>(override val compositeDisposable: CompositeDisposable):
        BaseMvpInteractor<P>(), IRxObject {

    override fun unbindPresenter() {
        super.unbindPresenter()
        compositeDisposable.clear()
    }
}