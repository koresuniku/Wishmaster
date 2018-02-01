package com.koresuniku.wishmaster_v4.application

import android.app.Application
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.koresuniku.wishmaster_v4.core.dagger.IWishmasterDaggerInjector
import com.koresuniku.wishmaster_v4.core.dagger.component.DaggerApplicationComponent
import com.koresuniku.wishmaster_v4.core.dagger.component.DaggerDashboardPresenterComponent
import com.koresuniku.wishmaster_v4.core.dagger.component.DaggerDashboardViewComponent
import com.koresuniku.wishmaster_v4.core.dagger.component.DaggerThreadListViewComponent
import com.koresuniku.wishmaster_v4.core.dagger.module.*
import com.koresuniku.wishmaster_v4.core.dagger.module.application_scope.ApplicationModule
import com.koresuniku.wishmaster_v4.core.dagger.module.application_scope.InjectorModule
import com.koresuniku.wishmaster_v4.core.dagger.module.application_scope.NetworkModule
import com.koresuniku.wishmaster_v4.core.dagger.module.application_scope.SharedPreferencesModule
import com.koresuniku.wishmaster_v4.core.dagger.module.dashboard_scope.BoardsModule
import com.koresuniku.wishmaster_v4.core.dagger.module.dashboard_scope.DashboardPresenterModule
import com.koresuniku.wishmaster_v4.core.dagger.module.dashboard_scope.DashboardViewModule
import com.koresuniku.wishmaster_v4.core.dagger.module.thread_list_scope.ThreadListModule
import com.koresuniku.wishmaster_v4.core.network.Dvach
import com.koresuniku.wishmaster_v4.core.network.client.RetrofitHolder
import com.squareup.leakcanary.LeakCanary
import okhttp3.OkHttpClient
import java.io.InputStream
import javax.inject.Inject

/**
 * Created by koresuniku on 03.10.17.
 */

/**
 * Ребята, не стоит вскрывать этот код. Вы молодые, шутливые, вам все легко. Это не то.
 * Это не Чикатило и даже не архивы спецслужб. Сюда лучше не лезть. Серьезно, любой из вас будет жалеть.
 * Лучше закройте код и забудьте, что тут писалось. Я вполне понимаю, что данным сообщением вызову дополнительный интерес,
 * но хочу сразу предостеречь пытливых - стоп. Остальные просто не найдут.
 */

class WishmasterDaggerApplication : Application(), IWishmasterDaggerInjector {

    private lateinit var mDaggerDashboardViewComponent: DaggerDashboardViewComponent
    private lateinit var mDaggerDashboardPresenterComponent: DaggerDashboardPresenterComponent
    private lateinit var mDaggerThreadListComponent: DaggerThreadListViewComponent
    private lateinit var mDaggerApplicationComponent: DaggerApplicationComponent

    @Inject lateinit var okHttpClient: OkHttpClient
    @Inject lateinit var ISharedPreferencesStorage: ISharedPreferencesStorage
    @Inject lateinit var retrofitHolder: RetrofitHolder

    override fun onCreate() {
        super.onCreate()

        if (!LeakCanary.isInAnalyzerProcess(this)) LeakCanary.install(this)

        mDaggerApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .injectorModule(InjectorModule(this))
                .networkModule(NetworkModule(Dvach.BASE_URL))
                .sharedPreferencesModule(SharedPreferencesModule())
                .build() as DaggerApplicationComponent
        mDaggerApplicationComponent.inject(this)

        SharedPreferencesHelper.onApplicationCreate(this, ISharedPreferencesStorage, retrofitHolder)

        mDaggerDashboardPresenterComponent = DaggerDashboardPresenterComponent.builder()
                .applicationComponent(mDaggerApplicationComponent)
                .dashboardPresenterModule(DashboardPresenterModule())
                .rxModule(RxModule())
                .boardsModule(BoardsModule())
                .searchModule(SearchModule())
                .build() as DaggerDashboardPresenterComponent

        mDaggerDashboardViewComponent = DaggerDashboardViewComponent.builder()
                .dashboardPresenterComponent(mDaggerDashboardPresenterComponent)
                .dashboardViewModule(DashboardViewModule())
                .build() as DaggerDashboardViewComponent

        mDaggerThreadListComponent = DaggerThreadListViewComponent.builder()
                .applicationComponent(mDaggerApplicationComponent)
                .threadListModule(ThreadListModule())
                .build() as DaggerThreadListViewComponent

        Glide.get(this).register(GlideUrl::class.java, InputStream::class.java, OkHttpUrlLoader.Factory(okHttpClient))
    }

    fun getDashboardViewComponent() = mDaggerDashboardViewComponent
    override fun getDashboardPresenterComponent() = mDaggerDashboardPresenterComponent
    override fun getThreadListComponent() = mDaggerThreadListComponent


}