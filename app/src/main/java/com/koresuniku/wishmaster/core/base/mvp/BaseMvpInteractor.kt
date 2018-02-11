package com.koresuniku.wishmaster.core.base.mvp


abstract class BaseMvpInteractor<P : IMvpPresenter<*>> : IMvpInteractor<P> {

    protected var presenter: P? = null

    override fun bindPresenter(presenter: P) {
        this.presenter = presenter
    }

    override fun unbindPresenter() {
        this.presenter = null
    }
}