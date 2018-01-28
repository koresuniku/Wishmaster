package com.koresuniku.wishmaster_v4.core.base

/**
 * Created by koresuniku on 03.10.17.
 */

abstract class BasePresenter<V: IMvpView> : IPresenter<V> {
    protected var mView: V? = null

    override fun bindView(mvpView: V) {
        this.mView = mvpView
    }

    override fun unbindView() {
        this.mView = null
    }
}