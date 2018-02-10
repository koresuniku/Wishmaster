package com.koresuniku.wishmaster_v4.core.dagger.component

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.koresuniku.wishmaster.domain.boards_api.BoardsApiService
import com.koresuniku.wishmaster_v4.application.preferences.ISharedPreferencesStorage
import com.koresuniku.wishmaster_v4.application.WishmasterApplication
import com.koresuniku.wishmaster_v4.application.listener.OrientationNotifier
import com.koresuniku.wishmaster_v4.application.preferences.SharedPreferencesStorage
import com.koresuniku.wishmaster_v4.application.preferences.UiParams
import com.koresuniku.wishmaster_v4.core.dagger.IWishmasterDaggerInjector
import com.koresuniku.wishmaster_v4.core.dagger.module.application_scope.*
import com.koresuniku.wishmaster_v4.core.data.database.DatabaseHelper
import com.koresuniku.wishmaster_v4.core.gallery.WishmasterImageUtils
import com.koresuniku.wishmaster_v4.core.network.client.RetrofitHolder
import com.koresuniku.wishmaster_v4.core.network.thread_list_api.ThreadListApiService
import com.koresuniku.wishmaster_v4.core.util.text.WishmasterTextUtils
import com.koresuniku.wishmaster_v4.ui.utils.DeviceUtils
import com.koresuniku.wishmaster_v4.ui.utils.UiUtils
import com.koresuniku.wishmaster_v4.ui.utils.ViewUtils
import dagger.Component
import okhttp3.OkHttpClient
import javax.inject.Singleton

/**
 * Created by koresuniku on 14.01.18.
 */

@Singleton
@Component (modules = [(ApplicationModule::class), (InjectorModule::class),
    (NetworkModule::class), (SharedPreferencesModule::class), (UtilsModule::class)])
interface ApplicationComponent {

    //Application
    fun application(): Application
    fun context(): Context
    fun injector(): IWishmasterDaggerInjector

    //SharedPreferences
    fun sharedPreferencesStorage(): SharedPreferencesStorage
    fun sharedPreferencesUiDimens(): UiParams

    //Database
    fun databaseHelper(): DatabaseHelper

    //Network
    fun retrofitHolder(): RetrofitHolder
    fun okHttpClient(): OkHttpClient
    fun gson(): Gson

    //API
    fun boardsApiService(): BoardsApiService
    fun threadListApiService(): ThreadListApiService

    //Utils
    fun textUtils(): WishmasterTextUtils
    fun imageUtils(): WishmasterImageUtils
    fun deviceUtils(): DeviceUtils
    fun uiUtils(): UiUtils
    fun viewUtils(): ViewUtils

    //Notifiers
    fun orientationNotifier(): OrientationNotifier

    fun inject(application: WishmasterApplication)

}