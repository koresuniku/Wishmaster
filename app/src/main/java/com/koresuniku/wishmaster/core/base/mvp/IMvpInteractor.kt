package com.koresuniku.wishmaster.core.base.mvp


interface IMvpInteractor<in P : IMvpPresenter<*>> {
    fun bindPresenter(presenter: P)
    fun unbindPresenter()
}