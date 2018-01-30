package com.koresuniku.wishmaster_v4.core.base

import io.reactivex.Single


interface INetworkInteractor<out S, M> {

    fun getService(): S
    fun getDataFromNetwork(): Single<M>
}