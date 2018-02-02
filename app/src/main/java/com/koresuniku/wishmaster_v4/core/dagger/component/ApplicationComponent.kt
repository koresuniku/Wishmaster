package com.koresuniku.wishmaster_v4.core.dagger.component

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.koresuniku.wishmaster.domain.boards_api.BoardsApiService
import com.koresuniku.wishmaster_v4.application.shared_preferences.ISharedPreferencesStorage
import com.koresuniku.wishmaster_v4.application.WishmasterDaggerApplication
import com.koresuniku.wishmaster_v4.core.dagger.IWishmasterDaggerInjector
import com.koresuniku.wishmaster_v4.core.dagger.module.application_scope.ApplicationModule
import com.koresuniku.wishmaster_v4.core.dagger.module.application_scope.InjectorModule
import com.koresuniku.wishmaster_v4.core.dagger.module.application_scope.NetworkModule
import com.koresuniku.wishmaster_v4.core.dagger.module.application_scope.SharedPreferencesModule
import com.koresuniku.wishmaster_v4.core.data.database.DatabaseHelper
import com.koresuniku.wishmaster_v4.core.network.thread_list_api.ThreadListApiService
import dagger.Component
import okhttp3.OkHttpClient
import javax.inject.Singleton

/**
 * Created by koresuniku on 14.01.18.
 */

@Singleton
@Component (modules = [(ApplicationModule::class), (InjectorModule::class),
    (NetworkModule::class), (SharedPreferencesModule::class)])
interface ApplicationComponent {

    //Application
    fun application(): Application
    fun context(): Context
    fun injector(): IWishmasterDaggerInjector

    //SharedPreferences
    fun sharedPreferencesStorage(): ISharedPreferencesStorage

    //Database
    fun databaseHelper(): DatabaseHelper

    //Network
    fun okHttpClient(): OkHttpClient
    fun gson(): Gson
    fun boardsApiServide(): BoardsApiService
    fun threadListApiService(): ThreadListApiService

    fun inject(application: WishmasterDaggerApplication)

}