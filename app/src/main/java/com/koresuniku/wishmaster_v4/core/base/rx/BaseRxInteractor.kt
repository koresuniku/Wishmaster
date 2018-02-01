package com.koresuniku.wishmaster_v4.core.base.rx

import com.koresuniku.wishmaster_v4.core.base.mvp.BaseMvpInteractor
import com.koresuniku.wishmaster_v4.core.base.mvp.MvpPresenter
import io.reactivex.disposables.CompositeDisposable


abstract class BaseRxInteractor<P : MvpPresenter<*>>(internal val compositeDisposable: CompositeDisposable):
        BaseMvpInteractor<P>(), IRxInteractor {

    override fun unbindPresenter() {
        super.unbindPresenter()
        compositeDisposable.clear()
    }

    override fun getCompositeDisposable(): CompositeDisposable = compositeDisposable
}