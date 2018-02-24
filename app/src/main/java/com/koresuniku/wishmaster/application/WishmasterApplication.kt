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
import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.google.firebase.messaging.FirebaseMessaging
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.application.notifier.NewReleaseNotifier
import com.koresuniku.wishmaster.application.notifier.OrientationNotifier
import com.koresuniku.wishmaster.application.preferences.*
import com.koresuniku.wishmaster.application.singletones.CommonParams
import com.koresuniku.wishmaster.application.singletones.UiParams
import com.koresuniku.wishmaster.application.utils.FirebaseKeystore
import com.koresuniku.wishmaster.core.dagger.IWishmasterDaggerInjector
import com.koresuniku.wishmaster.core.dagger.component.*
import com.koresuniku.wishmaster.core.dagger.module.*
import com.koresuniku.wishmaster.core.dagger.module.application_scope.*
import com.koresuniku.wishmaster.core.dagger.module.dashboard_scopes.BoardsModule
import com.koresuniku.wishmaster.core.dagger.module.dashboard_scopes.DashboardPresenterModule
import com.koresuniku.wishmaster.core.dagger.module.dashboard_scopes.DashboardViewModule
import com.koresuniku.wishmaster.core.dagger.module.full_thread_scopes.FullThreadPresenterModule
import com.koresuniku.wishmaster.core.dagger.module.full_thread_scopes.FullThreadViewModule
import com.koresuniku.wishmaster.core.dagger.module.settings_scopes.SettingsPresenterModule
import com.koresuniku.wishmaster.core.dagger.module.settings_scopes.SettingsViewModule
import com.koresuniku.wishmaster.core.dagger.module.thread_list_scopes.ThreadListPresenterModule
import com.koresuniku.wishmaster.core.dagger.module.thread_list_scopes.ThreadListViewModule
import com.koresuniku.wishmaster.core.network.client.RetrofitHolder
import com.koresuniku.wishmaster.core.network.github_api.GithubHelper
import com.koresuniku.wishmaster.ui.utils.DeviceUtils
import com.koresuniku.wishmaster.ui.utils.UiUtils
import com.koresuniku.wishmaster.ui.utils.ViewUtils
import io.reactivex.android.schedulers.AndroidSchedulers
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
 * Это не Чикатило и даже не архивы спецслужб. Сюда лучше не лезть. Серьезно, любой из вас будет жалеть.
 * Лучше закройте код и забудьте, что тут писалось. Я вполне понимаю, что данным сообщением вызову дополнительный интерес,
 * но хочу сразу предостеречь пытливых - стоп. Остальные просто не найдут.
 */

@ReportsCrashes(mailTo = "koresuniku@gmail.com")
class WishmasterApplication @Inject constructor() : Application(), IWishmasterDaggerInjector {

    val mDaggerApplicationComponent: DaggerApplicationComponent by lazy {
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
    override val daggerDashboardPresenterComponent: DaggerDashboardPresenterComponent by lazy {
        DaggerDashboardPresenterComponent.builder()
                .applicationComponent(mDaggerApplicationComponent)
                .dashboardPresenterModule(DashboardPresenterModule())
                .rxModule(RxModule())
                .boardsModule(BoardsModule())
                .searchModule(SearchModule())
                .build() as DaggerDashboardPresenterComponent
    }
    override val daggerDashboardViewComponent: DaggerDashboardViewComponent by lazy {
        DaggerDashboardViewComponent.builder()
                .dashboardPresenterComponent(daggerDashboardPresenterComponent)
                .dashboardViewModule(DashboardViewModule())
                .build() as DaggerDashboardViewComponent
    }
    override val daggerThreadListPresenterComponent: DaggerThreadListPresenterComponent by lazy {
        DaggerThreadListPresenterComponent.builder()
                .applicationComponent(mDaggerApplicationComponent)
                .threadListPresenterModule(ThreadListPresenterModule())
                .rxModule(RxModule())
                .build() as DaggerThreadListPresenterComponent
    }
    override val daggerThreadListViewComponent: DaggerThreadListViewComponent by lazy {
        DaggerThreadListViewComponent.builder()
                .threadListPresenterComponent(daggerThreadListPresenterComponent)
                .threadListViewModule(ThreadListViewModule())
                .build() as DaggerThreadListViewComponent
    }
    override val daggerFullThreadPresenterComponent: DaggerFullThreadPresenterComponent by lazy {
        DaggerFullThreadPresenterComponent.builder()
                .applicationComponent(mDaggerApplicationComponent)
                .fullThreadPresenterModule(FullThreadPresenterModule())
                .rxModule(RxModule())
                .build() as DaggerFullThreadPresenterComponent
    }
    override val daggerFullThreadViewComponent: DaggerFullThreadViewComponent by lazy {
        DaggerFullThreadViewComponent.builder()
                .fullThreadPresenterComponent(daggerFullThreadPresenterComponent)
                .fullThreadViewModule(FullThreadViewModule())
                .build() as DaggerFullThreadViewComponent
    }

    override val daggerSettingsPresenterComponent: DaggerSettingsPresenterComponent by lazy {
        DaggerSettingsPresenterComponent.builder()
                .applicationComponent(mDaggerApplicationComponent)
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
        //if (!LeakCanary.isInAnalyzerProcess(this)) LeakCanary.install(this)
        mDaggerApplicationComponent.inject(this)

        uiParams.orientation = resources.configuration.orientation
        sharedPreferencesHelper.onApplicationCreate(this, sharedPreferencesStorage,
                retrofitHolder, uiParams, commonParams, uiUtils, viewUtils, deviceUtils)

        githubHelper.checkForNewRelease()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { newReleaseNotifier.notifyNewVersion(it) }

        Glide.get(this).register(GlideUrl::class.java, InputStream::class.java, OkHttpUrlLoader.Factory(okHttpClient))
        FirebaseMessaging.getInstance().subscribeToTopic(FirebaseKeystore.NEW_VERSION_TOPIC)
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


}