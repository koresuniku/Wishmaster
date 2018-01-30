package com.koresuniku.wishmaster_v4.core.base.rx

import com.koresuniku.wishmaster_v4.core.base.INetworkInteractor
import com.koresuniku.wishmaster_v4.core.base.mvp.MvpPresenter
import io.reactivex.disposables.CompositeDisposable


abstract class BaseRxNetworkInteractor<P : MvpPresenter<*>, out S, M>(
        private val api: S, compositeDisposable: CompositeDisposable):
        BaseRxInteractor<P>(compositeDisposable), INetworkInteractor<S, M> {

    override fun getService(): S = api

}