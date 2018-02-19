/*
 * Copyright (c) 2018 koresuniku
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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