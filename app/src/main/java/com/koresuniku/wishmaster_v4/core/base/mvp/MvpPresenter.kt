package com.koresuniku.wishmaster_v4.core.base.mvp

/**
 * Created by koresuniku on 03.10.17.
 */

interface MvpPresenter<V: MvpView<*>> {
    fun bindView(mvpView: V)
    fun unbindView()
}