package com.koresuniku.wishmaster_v4.application

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.koresuniku.wishmaster_v4.R
import com.koresuniku.wishmaster_v4.application.listener.OrientationNotifier
import com.koresuniku.wishmaster_v4.application.preferences.*
import com.koresuniku.wishmaster_v4.core.dagger.IWishmasterDaggerInjector
import com.koresuniku.wishmaster_v4.core.dagger.component.*
import com.koresuniku.wishmaster_v4.core.dagger.module.*
import com.koresuniku.wishmaster_v4.core.dagger.module.application_scope.ApplicationModule
import com.koresuniku.wishmaster_v4.core.dagger.module.application_scope.InjectorModule
import com.koresuniku.wishmaster_v4.core.dagger.module.application_scope.NetworkModule
import com.koresuniku.wishmaster_v4.core.dagger.module.application_scope.SharedPreferencesModule
import com.koresuniku.wishmaster_v4.core.dagger.module.dashboard_scopes.BoardsModule
import com.koresuniku.wishmaster_v4.core.dagger.module.dashboard_scopes.DashboardPresenterModule
import com.koresuniku.wishmaster_v4.core.dagger.module.dashboard_scopes.DashboardViewModule
import com.koresuniku.wishmaster_v4.core.dagger.module.thread_list_scopes.ThreadListPresenterModule
import com.koresuniku.wishmaster_v4.core.dagger.module.thread_list_scopes.ThreadListViewModule
import com.koresuniku.wishmaster_v4.core.network.Dvach
import com.koresuniku.wishmaster_v4.core.network.client.RetrofitHolder
import com.koresuniku.wishmaster_v4.ui.utils.DeviceUtils
import com.koresuniku.wishmaster_v4.ui.utils.UiUtils
import com.koresuniku.wishmaster_v4.ui.utils.ViewUtils
import com.squareup.leakcanary.LeakCanary
import okhttp3.OkHttpClient
import org.acra.ACRA
import org.acra.ReportingInteractionMode
import org.acra.annotation.ReportsCrashes
import org.acra.config.ACRAConfiguration
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

    private val mDaggerApplicationComponent: DaggerApplicationComponent by lazy {
        DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .injectorModule(InjectorModule(this))
                .networkModule(NetworkModule())
                .sharedPreferencesModule(SharedPreferencesModule())
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

    @Inject lateinit var okHttpClient: OkHttpClient
    @Inject lateinit var sharedPreferencesStorage: SharedPreferencesStorage
    @Inject lateinit var uiParams: UiParams
    @Inject lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    @Inject lateinit var retrofitHolder: RetrofitHolder
    @Inject lateinit var orientationNotifier: OrientationNotifier
    @Inject lateinit var deviceUtils: DeviceUtils
    @Inject lateinit var uiUtils: UiUtils
    @Inject lateinit var viewUtils: ViewUtils

    override fun onCreate() {
        super.onCreate()

        if (!LeakCanary.isInAnalyzerProcess(this)) LeakCanary.install(this)

        mDaggerApplicationComponent.inject(this)

        //ACRA.init(this)
        //ACRA.getErrorReporter().setEnabled(false);

        uiParams.orientation = resources.configuration.orientation
        sharedPreferencesHelper.onApplicationCreate(this, sharedPreferencesStorage,
                retrofitHolder, uiParams, uiUtils, viewUtils, deviceUtils)

        Glide.get(this).register(GlideUrl::class.java, InputStream::class.java, OkHttpUrlLoader.Factory(okHttpClient))
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        val configurationBuilder = ConfigurationBuilder(this)
                .setResDialogTitle(R.string.crash_report)
                .setResDialogText(R.string.wishmaster_is_crashed)
                .setResDialogCommentPrompt(R.string.personal_comment)
                .setResNotifTickerText(R.string.app_name)
                .setResNotifTitle(R.string.app_name)
                .setResNotifText(R.string.send_report)
                .setResToastText(R.string.crash_report)
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