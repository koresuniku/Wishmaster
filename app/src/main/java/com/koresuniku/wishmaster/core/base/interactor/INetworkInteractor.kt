package com.koresuniku.wishmaster.core.base.interactor

import io.reactivex.Single


interface INetworkInteractor<out S, M> {

    fun getService(): S
    fun getDataFromNetwork(): Single<M>
}