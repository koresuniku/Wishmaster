package com.koresuniku.wishmaster_v4.core.dagger.module.dashboard_scope

import com.koresuniku.wishmaster_v4.core.dagger.scope.ForDashboardView
import com.koresuniku.wishmaster_v4.core.dagger.IWishmasterDaggerInjector
import com.koresuniku.wishmaster_v4.core.dashboard.interactor.DashboardDatabaseInteractor
import com.koresuniku.wishmaster_v4.core.dashboard.interactor.DashboardNetworkInteractor
import com.koresuniku.wishmaster_v4.core.dashboard.interactor.DashboardSearchInteractor
import com.koresuniku.wishmaster_v4.core.dashboard.interactor.DashboardSharedPreferencesInteractor
import com.koresuniku.wishmaster_v4.core.dashboard.presenter.DashboardPresenter
import com.koresuniku.wishmaster_v4.core.dashboard.presenter.IDashboardPresenter
import dagger.Module
import dagger.Provides

/**
 * Created by koresuniku on 05.10.17.
 */

@Module
class DashboardViewModule {

    @Provides
    @ForDashboardView
    fun provideDashboardPresenter(injector: IWishmasterDaggerInjector,
                                  dashboardNetworkInteractor: DashboardNetworkInteractor,
                                  dashboardDatabaseInteractor: DashboardDatabaseInteractor,
                                  dashboardSearchInteractor: DashboardSearchInteractor,
                                  dashboardSharedPreferencesInteractor: DashboardSharedPreferencesInteractor): IDashboardPresenter {
        return DashboardPresenter(injector, dashboardNetworkInteractor, dashboardDatabaseInteractor, dashboardSearchInteractor, dashboardSharedPreferencesInteractor)
    }
}