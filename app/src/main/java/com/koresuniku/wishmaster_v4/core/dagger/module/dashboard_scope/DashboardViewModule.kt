package com.koresuniku.wishmaster_v4.core.dagger.module.dashboard_scope

import com.koresuniku.wishmaster_v4.core.dagger.scope.ForDashboardView
import com.koresuniku.wishmaster_v4.core.dagger.IWishmasterDaggerInjector
import com.koresuniku.wishmaster_v4.core.dashboard.interactor.DashboardDatabaseInteractorI
import com.koresuniku.wishmaster_v4.core.dashboard.interactor.DashboardNetworkInteractorI
import com.koresuniku.wishmaster_v4.core.dashboard.interactor.DashboardSearchInteractorI
import com.koresuniku.wishmaster_v4.core.dashboard.interactor.DashboardSharedPreferencesInteractorI
import com.koresuniku.wishmaster_v4.core.dashboard.presenter.DashboardPresenterI
import com.koresuniku.wishmaster_v4.core.dashboard.presenter.IDashboardPresenterI
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
                                  dashboardNetworkInteractor: DashboardNetworkInteractorI,
                                  dashboardDatabaseInteractor: DashboardDatabaseInteractorI,
                                  dashboardSearchInteractor: DashboardSearchInteractorI,
                                  dashboardSharedPreferencesInteractor: DashboardSharedPreferencesInteractorI): IDashboardPresenterI {
        return DashboardPresenterI(injector, dashboardNetworkInteractor, dashboardDatabaseInteractor, dashboardSearchInteractor, dashboardSharedPreferencesInteractor)
    }
}