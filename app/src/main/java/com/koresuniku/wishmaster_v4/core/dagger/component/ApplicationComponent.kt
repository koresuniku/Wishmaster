package com.koresuniku.wishmaster_v4.core.dagger.component

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.koresuniku.wishmaster.domain.boards_api.BoardsApiService
import com.koresuniku.wishmaster_v4.application.ISharedPreferencesStorage
import com.koresuniku.wishmaster_v4.application.WishmasterApplication
import com.koresuniku.wishmaster_v4.core.dagger.module.ApplicationModule
import com.koresuniku.wishmaster_v4.core.dagger.module.NetworkModule
import com.koresuniku.wishmaster_v4.core.dagger.module.SharedPreferencesModule
import com.koresuniku.wishmaster_v4.core.data.database.DatabaseHelper
import com.koresuniku.wishmaster_v4.core.domain.thread_list_api.ThreadListApiService
import dagger.Component
import okhttp3.OkHttpClient
import javax.inject.Singleton

/**
 * Created by koresuniku on 14.01.18.
 */

@Singleton
@Component (modules = [(ApplicationModule::class), (NetworkModule::class), (SharedPreferencesModule::class)])
interface ApplicationComponent {

    fun application(): Application
    fun context(): Context
    fun sharedPreferencesStorage(): ISharedPreferencesStorage
    fun databaseHelper(): DatabaseHelper
    fun boardsApiServide(): BoardsApiService
    fun threadListApiService(): ThreadListApiService
    fun okHttpClient(): OkHttpClient
    fun gson(): Gson

    fun inject(application: WishmasterApplication)

}