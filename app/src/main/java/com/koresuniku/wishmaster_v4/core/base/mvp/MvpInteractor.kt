package com.koresuniku.wishmaster_v4.core.base.mvp


interface MvpInteractor<in P : MvpPresenter<*>> {
    fun bindPresenter(presenter: P)
    fun unbindPresenter()
}