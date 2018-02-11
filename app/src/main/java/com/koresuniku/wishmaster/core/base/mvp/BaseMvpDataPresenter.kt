package com.koresuniku.wishmaster.core.base.mvp

/**
 * Created by koresuniku on 02.02.18.
 */
abstract class BaseMvpDataPresenter<V: IMvpView<*>, D> : BaseMvpPresenter<V>(), IMvpDataPresenter<D> {

    override fun unbindView() {
        super.unbindView()
    }
}