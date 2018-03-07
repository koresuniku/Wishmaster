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

package com.koresuniku.wishmaster.application

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.koresuniku.wishmaster.domain.boards_api.BoardsApiService
import com.koresuniku.wishmaster.application.WishmasterApplication
import com.koresuniku.wishmaster.application.notifier.NewReleaseNotifier
import com.koresuniku.wishmaster.application.notifier.OrientationNotifier
import com.koresuniku.wishmaster.application.preferences.ISharedPreferencesStorage
import com.koresuniku.wishmaster.application.service.WMFirebaseMessagingService
import com.koresuniku.wishmaster.application.singletones.UiParams
import com.koresuniku.wishmaster.application.singletones.WMDownloadManager
import com.koresuniku.wishmaster.application.singletones.WMPermissionManager
import com.koresuniku.wishmaster.application.utils.StubActivity
import com.koresuniku.wishmaster.core.dagger.IWishmasterDaggerInjector
import com.koresuniku.wishmaster.core.dagger.module.DownloaderModule
import com.koresuniku.wishmaster.core.dagger.module.RxModule
import com.koresuniku.wishmaster.core.dagger.module.application_scope.*
import com.koresuniku.wishmaster.core.data.database.DatabaseHelper
import com.koresuniku.wishmaster.core.utils.images.WMImageUtils
import com.koresuniku.wishmaster.core.data.network.client.RetrofitHolder
import com.koresuniku.wishmaster.core.data.network.full_thread_api.FullThreadApiService
import com.koresuniku.wishmaster.core.data.network.github.GithubModule
import com.koresuniku.wishmaster.core.data.network.thread_list_api.ThreadListApiService
import com.koresuniku.wishmaster.core.utils.text.WMTextUtils
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
    (RxModule::class),
    (DownloaderModule::class)])
interface ApplicationComponent {

    //Application
    fun application(): Application
    fun context(): Context
    fun injector(): IWishmasterDaggerInjector

    //SharedPreferences
    fun sharedPreferencesStorage(): ISharedPreferencesStorage
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
    fun textUtils(): WMTextUtils
    fun imageUtils(): WMImageUtils
    fun deviceUtils(): DeviceUtils
    fun uiUtils(): UiUtils
    fun viewUtils(): ViewUtils
    fun animationUtils(): WishmasterAnimationUtils
    fun downloadManager(): WMDownloadManager
    fun permissionManager(): WMPermissionManager

    //Notifiers
    fun orientationNotifier(): OrientationNotifier
    fun newReleaseNotifier(): NewReleaseNotifier

    fun inject(application: WishmasterApplication)
    fun inject(service: WMFirebaseMessagingService)
    fun inject(service: WMFirebaseMessagingService.DownloadIntentService)
    fun inject(activity: StubActivity)

}