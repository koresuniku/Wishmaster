package com.koresuniku.wishmaster_v4.core.base.mvp


abstract class BaseMvpInteractor<P : MvpPresenter<*>> : MvpInteractor<P> {

    protected var presenter: P? = null

    override fun bindPresenter(presenter: P) {
        this.presenter = presenter
    }

    override fun unbindPresenter() {
        this.presenter = null
    }
}