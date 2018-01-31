package com.koresuniku.wishmaster_v4.core.dagger.module

import com.koresuniku.wishmaster_v4.core.dagger.ForDashboardView
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
    fun provideDashboardPresenter(dashboardNetworkInteractor: DashboardNetworkInteractor,
                                  dashboardDatabaseInteractor: DashboardDatabaseInteractor,
                                  dashboardSearchInteractor: DashboardSearchInteractor): IDashboardPresenter {
        return DashboardPresenter(dashboardNetworkInteractor, dashboardDatabaseInteractor, dashboardSearchInteractor)
    }
}