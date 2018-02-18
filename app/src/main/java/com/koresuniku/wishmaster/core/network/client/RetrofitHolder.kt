package com.koresuniku.wishmaster.core.network.client

import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.koresuniku.wishmaster.core.network.Dvach
import com.koresuniku.wishmaster.core.network.github_api.GithubHelper
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

/**
 * Created by koresuniku on 15.01.18.
 */

class RetrofitHolder @Inject constructor(val gson: Gson, val okHttpClient: OkHttpClient) {
    private var mDvachRetrofit: Retrofit
    private var mGithubRetrofit: Retrofit

    init {
        mDvachRetrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(Dvach.BASE_URL)
                .client(okHttpClient)
                .build()

        mGithubRetrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(GithubHelper.GITHUB_BASE_URL)
                .client(okHttpClient)
                .build()
    }

    fun getDvachRetrofit() = mDvachRetrofit
    fun getGithubRetrofit() = mGithubRetrofit

    fun changeDvachBaseUrl(newBaseUrl: String) {
        mDvachRetrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(newBaseUrl)
                .client(okHttpClient)
                .build()
    }

    fun getDvachBaseUrl(): String = mDvachRetrofit.baseUrl().let { return@let "${it.scheme()}://${it.host()}"}
}