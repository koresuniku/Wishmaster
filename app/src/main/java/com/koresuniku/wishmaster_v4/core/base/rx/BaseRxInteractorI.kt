package com.koresuniku.wishmaster_v4.core.base.rx

import com.koresuniku.wishmaster_v4.core.base.mvp.BaseIMvpInteractor
import com.koresuniku.wishmaster_v4.core.base.mvp.IMvpPresenter
import io.reactivex.disposables.CompositeDisposable


abstract class BaseRxInteractorI<P : IMvpPresenter<*>>(internal val compositeDisposable: CompositeDisposable):
        BaseIMvpInteractor<P>(), IRxInteractor {

    override fun unbindPresenter() {
        super.unbindPresenter()
        compositeDisposable.clear()
    }

    override fun getCompositeDisposable(): CompositeDisposable = compositeDisposable
}