package com.koresuniku.wishmaster.core.dagger.component

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.koresuniku.wishmaster.domain.boards_api.BoardsApiService
import com.koresuniku.wishmaster.application.WishmasterApplication
import com.koresuniku.wishmaster.application.listener.OrientationNotifier
import com.koresuniku.wishmaster.application.preferences.SharedPreferencesStorage
import com.koresuniku.wishmaster.application.preferences.UiParams
import com.koresuniku.wishmaster.core.dagger.IWishmasterDaggerInjector
import com.koresuniku.wishmaster.core.dagger.module.RxModule
import com.koresuniku.wishmaster.core.dagger.module.application_scope.*
import com.koresuniku.wishmaster.core.data.database.DatabaseHelper
import com.koresuniku.wishmaster.core.utils.images.WishmasterImageUtils
import com.koresuniku.wishmaster.core.network.client.RetrofitHolder
import com.koresuniku.wishmaster.core.network.full_thread_api.FullThreadApiService
import com.koresuniku.wishmaster.core.network.thread_list_api.ThreadListApiService
import com.koresuniku.wishmaster.core.utils.text.WishmasterTextUtils
import com.koresuniku.wishmaster.ui.anim.WishmasterAnimationUtils
import com.koresuniku.wishmaster.ui.utils.DeviceUtils
import com.koresuniku.wishmaster.ui.utils.UiUtils
import com.koresuniku.wishmaster.ui.utils.ViewUtils
import dagger.Component
import okhttp3.OkHttpClient
import javax.inject.Singleton

/**
 * Created by koresuniku on 14.01.18.
 */

@Singleton
@Component (modules = [
    (ApplicationModule::class),
    (InjectorModule::class),
    (NetworkModule::class),
    (SharedPreferencesModule::class),
    (UtilsModule::class),
    (GithubModule::class),
    (RxModule::class)])
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
    fun fullThreadApiService(): FullThreadApiService

    //Utils
    fun textUtils(): WishmasterTextUtils
    fun imageUtils(): WishmasterImageUtils
    fun deviceUtils(): DeviceUtils
    fun uiUtils(): UiUtils
    fun viewUtils(): ViewUtils
    fun animationUtils(): WishmasterAnimationUtils

    //Notifiers
    fun orientationNotifier(): OrientationNotifier

    fun inject(application: WishmasterApplication)

}