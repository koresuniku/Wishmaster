package com.koresuniku.wishmaster.core.base.mvp

/**
 * Created by koresuniku on 03.10.17.
 */

interface IMvpPresenter<V: IMvpView<*>> {
    fun bindView(mvpView: V)
    fun unbindView()
}