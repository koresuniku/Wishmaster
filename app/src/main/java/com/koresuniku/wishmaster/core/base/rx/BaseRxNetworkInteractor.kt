package com.koresuniku.wishmaster.core.base.rx

import com.koresuniku.wishmaster.core.base.interactor.INetworkInteractor
import com.koresuniku.wishmaster.core.base.mvp.IMvpPresenter
import io.reactivex.disposables.CompositeDisposable


abstract class BaseRxNetworkInteractor<P : IMvpPresenter<*>, out S, M>(
        private val api: S, compositeDisposable: CompositeDisposable):
        BaseRxInteractor<P>(compositeDisposable), INetworkInteractor<S, M> {

    override fun getService(): S = api

}