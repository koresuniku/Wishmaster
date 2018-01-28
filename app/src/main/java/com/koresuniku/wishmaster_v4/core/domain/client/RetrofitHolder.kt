package com.koresuniku.wishmaster_v4.core.domain.client

import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.koresuniku.wishmaster_v4.core.domain.Dvach
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

/**
 * Created by koresuniku on 15.01.18.
 */

class RetrofitHolder @Inject constructor(val gson: Gson, val okHttpClient: OkHttpClient) {
    private var mRetrofit: Retrofit

    init {
        mRetrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(Dvach.BASE_URL)
                .client(okHttpClient)
                .build()
    }

    fun getRetrofit() = mRetrofit

    fun changeBaseUrl(newBaseUrl: String) {
        mRetrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(newBaseUrl)
                .client(okHttpClient)
                .build()
    }

    fun getBaseUrl(): String = mRetrofit.baseUrl().let { return@let "${it.scheme()}://${it.host()}"}

}