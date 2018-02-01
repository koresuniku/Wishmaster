package com.koresuniku.wishmaster_v4.core.base.mvp


interface IMvpInteractor<in P : IMvpPresenter<*>> {
    fun bindPresenter(presenter: P)
    fun unbindPresenter()
}