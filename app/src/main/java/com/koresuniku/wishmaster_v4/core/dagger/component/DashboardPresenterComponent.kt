package com.koresuniku.wishmaster_v4.core.dagger.component

import com.koresuniku.wishmaster_v4.core.dagger.scope.ForDashboardPresenter
import com.koresuniku.wishmaster_v4.core.dagger.IWishmasterDaggerInjector
import com.koresuniku.wishmaster_v4.core.dagger.module.dashboard_scope.BoardsModule
import com.koresuniku.wishmaster_v4.core.dagger.module.dashboard_scope.DashboardPresenterModule
import com.koresuniku.wishmaster_v4.core.dagger.module.RxModule
import com.koresuniku.wishmaster_v4.core.dagger.module.SearchModule
import com.koresuniku.wishmaster_v4.core.dashboard.interactor.DashboardDatabaseInteractor
import com.koresuniku.wishmaster_v4.core.dashboard.interactor.DashboardNetworkInteractor
import com.koresuniku.wishmaster_v4.core.dashboard.interactor.DashboardSearchInteractor
import com.koresuniku.wishmaster_v4.core.dashboard.interactor.DashboardSharedPreferencesInteractor
import com.koresuniku.wishmaster_v4.core.dashboard.presenter.DashboardPresenter
import dagger.Component


@ForDashboardPresenter
@Component(
        dependencies = [ApplicationComponent::class],
        modules = [DashboardPresenterModule::class, RxModule::class, BoardsModule::class, SearchModule::class])
interface DashboardPresenterComponent {

    fun injector(): IWishmasterDaggerInjector
    fun dashboardNetworkInteractor(): DashboardNetworkInteractor
    fun dashboardDatabaseInteractor(): DashboardDatabaseInteractor
    fun dashboardSearchInteractor(): DashboardSearchInteractor
    fun dashboardSharedPreferencesInteractor(): DashboardSharedPreferencesInteractor

    fun inject(dashboardPresenter: DashboardPresenter)

}