package com.koresuniku.wishmaster.core.dagger.module.application_scope

import android.app.Application
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.koresuniku.wishmaster.domain.boards_api.BoardsApiService
import com.koresuniku.wishmaster.application.preferences.CommonParams
import com.koresuniku.wishmaster.core.network.Dvach
import com.koresuniku.wishmaster.core.network.client.RetrofitHolder
import com.koresuniku.wishmaster.core.network.full_thread_api.FullThreadApiService
import com.koresuniku.wishmaster.core.network.github_api.GithubApiService
import com.koresuniku.wishmaster.core.network.github_api.GithubHelper
import com.koresuniku.wishmaster.core.network.thread_list_api.ThreadListApiService
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
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