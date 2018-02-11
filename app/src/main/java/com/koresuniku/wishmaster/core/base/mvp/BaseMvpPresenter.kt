package com.koresuniku.wishmaster.core.base.mvp

/**
 * Created by koresuniku on 03.10.17.
 */

abstract class BaseMvpPresenter<V: IMvpView<*>> : IMvpPresenter<V> {
    protected var mvpView: V? = null

    override fun bindView(mvpView: V) {
        this.mvpView = mvpView
    }

    override fun unbindView() {
        this.mvpView = null
    }
}