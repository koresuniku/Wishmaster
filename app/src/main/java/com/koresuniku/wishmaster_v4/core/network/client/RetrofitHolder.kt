package com.koresuniku.wishmaster_v4.core.network.client

import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.koresuniku.wishmaster_v4.core.dagger.BaseDaggerMutableHolder
import com.koresuniku.wishmaster_v4.core.dagger.IDaggerMutableHolder
import com.koresuniku.wishmaster_v4.core.network.Dvach
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

/**
 * Created by koresuniku on 15.01.18.
 */

class RetrofitHolder @Inject constructor(private val gson: Gson,
                                         private val okHttpClient: OkHttpClient): BaseDaggerMutableHolder<Retrofit>() {

    override fun initObject(): Retrofit {
        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(Dvach.BASE_URL)
                .client(okHttpClient)
                .build()
    }

    fun changeBaseUrl(newBaseUrl: String) {
         setObject(Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(newBaseUrl)
                .client(okHttpClient)
                .build())
    }

    fun getBaseUrl(): String = getObject().baseUrl().let { return@let "${it.scheme()}://${it.host()}"}

}