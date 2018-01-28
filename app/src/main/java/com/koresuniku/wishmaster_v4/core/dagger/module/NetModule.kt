package com.koresuniku.wishmaster_v4.core.dagger.module

import android.app.Application
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.koresuniku.wishmaster.domain.boards_api.BoardsApiService
import com.koresuniku.wishmaster_v4.core.domain.Dvach
import com.koresuniku.wishmaster_v4.core.domain.client.HostSelectionInterceptor
import com.koresuniku.wishmaster_v4.core.domain.client.RetrofitHolder
import com.koresuniku.wishmaster_v4.core.domain.thread_list_api.ThreadListApiService
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Created by koresuniku on 03.10.17.
 */

@Module
class NetModule(val mBaseUrl: String) {

    @Provides
    @Singleton
    fun provideHttpCache(application: Application): Cache {
        val cacheSize: Int = 10 * 1024 * 1024
        return Cache(application.cacheDir, cacheSize.toLong())
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        return gsonBuilder.create()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(cache: Cache): OkHttpClient {
        val client = OkHttpClient.Builder()
        client.cache(cache)
        return client.build()
    }

    @Provides
    @Singleton
    fun provideRetrofitHolder(gson: Gson, okHttpClient: OkHttpClient): RetrofitHolder = RetrofitHolder(gson, okHttpClient)

    @Provides
    @Singleton
    fun provideRetrofit(retrofitHolder: RetrofitHolder): Retrofit = retrofitHolder.getRetrofit()

    @Provides
    @Singleton
    fun provideBoardsApi(retrofit: Retrofit): BoardsApiService =
            retrofit.create(BoardsApiService::class.java)

    @Provides
    @Singleton
    fun provideThreadListApi(retrofit: Retrofit): ThreadListApiService =
            retrofit.create(ThreadListApiService::class.java)
}