package com.koresuniku.wishmaster_v4.core.dagger.module

import com.koresuniku.wishmaster_v4.core.dagger.scope.ForDashboardView
import com.koresuniku.wishmaster_v4.core.dagger.IWishmasterInjector
import com.koresuniku.wishmaster_v4.core.dashboard.*
import dagger.Module
import dagger.Provides

/**
 * Created by koresuniku on 05.10.17.
 */

@Module
class DashboardViewModule {

    @Provides
    @ForDashboardView
    fun provideDashboardPresenter(injector: IWishmasterInjector,
                                  dashboardNetworkInteractor: DashboardNetworkInteractor,
                                  dashboardDatabaseInteractor: DashboardDatabaseInteractor,
                                  dashboardSearchInteractor: DashboardSearchInteractor,
                                  dashboardSharedPreferencesInteractor: DashboardSharedPreferencesInteractor): IDashboardPresenter {
        return DashboardPresenter(injector, dashboardNetworkInteractor, dashboardDatabaseInteractor, dashboardSearchInteractor, dashboardSharedPreferencesInteractor)
    }
}