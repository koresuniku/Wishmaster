package com.koresuniku.wishmaster_v4.core.base.mvp

/**
 * Created by koresuniku on 03.10.17.
 */

interface MvpPresenter<in V: IMvpView> {
    fun bindView(mvpView: V)
    fun unbindView()
}