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

import android.content.Context
import android.content.res.Configuration
import android.support.multidex.MultiDexApplication
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.github.piasy.biv.BigImageViewer
import com.github.piasy.biv.loader.fresco.FrescoImageLoader
import com.google.firebase.FirebaseApp
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.application.notifier.NewReleaseNotifier
import com.koresuniku.wishmaster.application.notifier.OrientationNotifier
import com.koresuniku.wishmaster.application.preferences.*
import com.koresuniku.wishmaster.application.global.CommonParams
import com.koresuniku.wishmaster.application.global.DownloaderModule
import com.koresuniku.wishmaster.application.global.RxModule
import com.koresuniku.wishmaster.application.global.UiParams
import com.koresuniku.wishmaster.core.data.network.NetworkModule
import com.koresuniku.wishmaster.core.module.settings.SettingsPresenterModule
import com.koresuniku.wishmaster.core.module.settings.SettingsViewModule
import com.koresuniku.wishmaster.core.module.dashboard.*
import com.koresuniku.wishmaster.core.module.full_thread.*
import com.koresuniku.wishmaster.core.module.thread_list.*
import com.koresuniku.wishmaster.core.data.network.client.RetrofitHolder
import com.koresuniku.wishmaster.core.data.network.github.GithubHelper
import com.koresuniku.wishmaster.core.data.network.github.GithubModule
import com.koresuniku.wishmaster.core.module.gallery.*
import com.koresuniku.wishmaster.core.module.settings.DaggerSettingsPresenterComponent
import com.koresuniku.wishmaster.core.module.settings.DaggerSettingsViewComponent
import com.koresuniku.wishmaster.ui.utils.DeviceUtils
import com.koresuniku.wishmaster.ui.utils.UiUtils
import com.koresuniku.wishmaster.ui.utils.ViewUtils
import com.squareup.leakcanary.LeakCanary
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import org.acra.ACRA
import org.acra.ReportingInteractionMode
import org.acra.annotation.ReportsCrashes
import java.io.InputStream
import javax.inject.Inject
import org.acra.config.ConfigurationBuilder



/**
 * Created by koresuniku on 03.10.17.
 */

/**
 * Ребята, не стоит вскрывать этот код. Вы молодые, шутливые, вам все легко. Это не то.
 * Это не Чикатило и даже не архивы спецслужб. Сюда лучше не лезть.
 * Серьезно, любой из вас будет жалеть.
 * Лучше закройте код и забудьте, что тут писалось.
 * Я вполне понимаю, что данным сообщением вызову дополнительный интерес,
 * но хочу сразу предостеречь пытливых - стоп. Остальные просто не найдут.
 */

@ReportsCrashes(mailTo = "koresuniku@gmail.com")
class WMApplication @Inject constructor() : MultiDexApplication(), IWMDependencyInjector {

    override val daggerApplicationComponent: DaggerApplicationComponent by lazy {
        DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .injectorModule(InjectorModule(this))
                .networkModule(NetworkModule())
                .sharedPreferencesModule(SharedPreferencesModule())
                .rxModule(RxModule())
                .githubModule(GithubModule())
                .downloaderModule(DownloaderModule(this))
                .build() as DaggerApplicationComponent
    }
    override val daggerDashboardLogicComponent: DaggerDashboardLogicComponent by lazy {
        DaggerDashboardLogicComponent.builder()
                .applicationComponent(daggerApplicationComponent)
                .dashboardLogicModule(DashboardLogicModule())
                .rxModule(RxModule())
                .searchModule(SearchModule())
                .build() as DaggerDashboardLogicComponent
    }
    override val daggerDashboardPresenterComponent: DaggerDashboardPresenterComponent by lazy {
        DaggerDashboardPresenterComponent.builder()
                .dashboardLogicComponent(daggerDashboardLogicComponent)
                .build() as DaggerDashboardPresenterComponent
    }
    override val daggerDashboardViewComponent: DaggerDashboardViewComponent by lazy {
        DaggerDashboardViewComponent.builder()
                .dashboardPresenterComponent(daggerDashboardPresenterComponent)
                .dashboardViewModule(DashboardViewModule())
                .build() as DaggerDashboardViewComponent
    }

    override lateinit var daggerThreadListLogicComponent: DaggerThreadListLogicComponent
    override lateinit var daggerThreadListPresenterComponent:DaggerThreadListPresenterComponent
    override lateinit var daggerThreadListViewComponent: DaggerThreadListViewComponent

    override lateinit var daggerFullThreadLogicComponent: DaggerFullThreadLogicComponent
    override lateinit var daggerFullThreadPresenterComponent: DaggerFullThreadPresenterComponent
    override lateinit var daggerFullThreadViewComponent: DaggerFullThreadViewComponent

    override val daggerSettingsPresenterComponent: DaggerSettingsPresenterComponent by lazy {
        DaggerSettingsPresenterComponent.builder()
                .applicationComponent(daggerApplicationComponent)
                .settingsPresenterModule(SettingsPresenterModule())
                .rxModule(RxModule())
                .build() as DaggerSettingsPresenterComponent
    }
    override val daggerSettingsViewComponent: DaggerSettingsViewComponent by lazy {
        DaggerSettingsViewComponent.builder()
                .settingsPresenterComponent(daggerSettingsPresenterComponent)
                .settingsViewModule(SettingsViewModule())
                .build() as DaggerSettingsViewComponent
    }

    @Inject lateinit var okHttpClient: OkHttpClient
    @Inject lateinit var sharedPreferencesStorage: SharedPreferencesStorage
    @Inject lateinit var uiParams: UiParams
    @Inject lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    @Inject lateinit var retrofitHolder: RetrofitHolder
    @Inject lateinit var orientationNotifier: OrientationNotifier
    @Inject lateinit var deviceUtils: DeviceUtils
    @Inject lateinit var uiUtils: UiUtils
    @Inject lateinit var viewUtils: ViewUtils
    @Inject lateinit var commonParams: CommonParams
    @Inject lateinit var githubHelper: GithubHelper
    @Inject lateinit var newReleaseNotifier: NewReleaseNotifier

    override fun onCreate() {
        super.onCreate()
        if (!LeakCanary.isInAnalyzerProcess(this)) LeakCanary.install(this)
        daggerApplicationComponent.inject(this)
        FirebaseApp.initializeApp(this)

        uiParams.orientation = resources.configuration.orientation
        sharedPreferencesHelper.onApplicationCreate(this, sharedPreferencesStorage,
                retrofitHolder, uiParams, commonParams, uiUtils, viewUtils, deviceUtils)

        githubHelper.checkForNewRelease()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newReleaseNotifier::notifyNewVersion)

        Glide.get(this).register(
                GlideUrl::class.java,
                InputStream::class.java,
                OkHttpUrlLoader.Factory(okHttpClient))
        BigImageViewer.initialize(FrescoImageLoader.with(this))
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        val configurationBuilder = ConfigurationBuilder(this)
                .setResDialogTitle(R.string.crash_report)
                .setResDialogText(R.string.wishmaster_is_crashed)
                .setResDialogCommentPrompt(R.string.personal_comment)
                .setResDialogPositiveButtonText(R.string.send_text)
                .setResNotifTickerText(R.string.app_name)
                .setResNotifTitle(R.string.app_name)
                .setResNotifText(R.string.send_report)
                .setResToastText(R.string.building_crash_report)
                .setReportingInteractionMode(ReportingInteractionMode.NOTIFICATION)
        val configuration = configurationBuilder.build()
        ACRA.init(this, configuration, true)
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        newConfig?.let {
            uiParams.orientation = it.orientation
            orientationNotifier.notifyOrientation(it.orientation)
        }
    }

    override fun requestThreadListModule() {
        daggerThreadListLogicComponent = DaggerThreadListLogicComponent.builder()
                .applicationComponent(daggerApplicationComponent)
                .threadListLogicModule(ThreadListLogicModule())
                .galleryLogicModule(GalleryLogicModule())
                .rxModule(RxModule())
                .build() as DaggerThreadListLogicComponent

        daggerThreadListPresenterComponent = DaggerThreadListPresenterComponent.builder()
                .threadListLogicComponent(daggerThreadListLogicComponent)
                .galleryPresenterModule(GalleryPresenterModule(daggerThreadListLogicComponent))
                .build() as DaggerThreadListPresenterComponent

        daggerThreadListViewComponent = DaggerThreadListViewComponent.builder()
                .threadListPresenterComponent(daggerThreadListPresenterComponent)
                .threadListViewModule(ThreadListViewModule())
                .galleryViewModule(GalleryViewModule(daggerThreadListPresenterComponent))
                .build() as DaggerThreadListViewComponent
    }

    override fun requestFullThreadModule() {
        daggerFullThreadLogicComponent = DaggerFullThreadLogicComponent.builder()
                .applicationComponent(daggerApplicationComponent)
                .fullThreadLogicModule(FullThreadLogicModule())
                .galleryLogicModule(GalleryLogicModule())
                .rxModule(RxModule())
                .build() as DaggerFullThreadLogicComponent

        daggerFullThreadPresenterComponent = DaggerFullThreadPresenterComponent.builder()
                .fullThreadLogicComponent(daggerFullThreadLogicComponent)
                .galleryPresenterModule(GalleryPresenterModule(daggerFullThreadLogicComponent))
                .build() as DaggerFullThreadPresenterComponent

        daggerFullThreadViewComponent = DaggerFullThreadViewComponent.builder()
                .fullThreadPresenterComponent(daggerFullThreadPresenterComponent)
                .fullThreadViewModule(FullThreadViewModule())
                .galleryViewModule(GalleryViewModule(daggerFullThreadPresenterComponent))
                .build() as DaggerFullThreadViewComponent
    }
}