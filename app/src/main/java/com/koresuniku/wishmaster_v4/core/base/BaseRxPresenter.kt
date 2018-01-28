package com.koresuniku.wishmaster_v4.core.base

import io.reactivex.disposables.CompositeDisposable

/**
 * Created by koresuniku on 12.11.17.
 */

abstract class BaseRxPresenter<V: IMvpView> : BasePresenter<V>() {
    protected lateinit var compositeDisposable: CompositeDisposable

    override fun bindView(mvpView: V) {
        super.bindView(mvpView)
        compositeDisposable = CompositeDisposable()
    }

    override fun unbindView() {
        super.unbindView()
        compositeDisposable.clear()
    }
}