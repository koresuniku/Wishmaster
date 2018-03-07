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

package com.koresuniku.wishmaster.core.data.network

import android.app.Application
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.koresuniku.wishmaster.domain.boards_api.BoardsApiService
import com.koresuniku.wishmaster.application.global.CommonParams
import com.koresuniku.wishmaster.core.data.network.client.RetrofitHolder
import com.koresuniku.wishmaster.core.data.network.full_thread_api.FullThreadApiService
import com.koresuniku.wishmaster.core.data.network.github.GithubApiService
import com.koresuniku.wishmaster.core.data.network.thread_list_api.ThreadListApiService
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import javax.inject.Singleton

/**
 * Created by koresuniku on 03.10.17.
 */

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideHttpCache(application: Application, commonParams: CommonParams): Cache {
        return Cache(application.cacheDir, commonParams.cacheSize.toLong())
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
    fun provideRetrofitHolder(gson: Gson, okHttpClient: OkHttpClient): RetrofitHolder =
            RetrofitHolder(gson, okHttpClient)

    @Provides
    @Singleton
    fun provideBoardsApi(retrofitHolder: RetrofitHolder): BoardsApiService {
        return retrofitHolder.getDvachRetrofit().create(BoardsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideThreadListApi(retrofitHolder: RetrofitHolder): ThreadListApiService {
        return retrofitHolder.getDvachRetrofit().create(ThreadListApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideFullThreadApi(retrofitHolder: RetrofitHolder): FullThreadApiService {
        return retrofitHolder.getDvachRetrofit().create(FullThreadApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideGithubApi(retrofitHolder: RetrofitHolder): GithubApiService {
        return retrofitHolder.getGithubRetrofit().create(GithubApiService::class.java)
    }
}