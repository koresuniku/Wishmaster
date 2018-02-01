package com.koresuniku.wishmaster_v4.core.dagger.component

import com.koresuniku.wishmaster_v4.core.dagger.scope.ForDashboardPresenter
import com.koresuniku.wishmaster_v4.core.dagger.IWishmasterDaggerInjector
import com.koresuniku.wishmaster_v4.core.dagger.module.dashboard_scope.BoardsModule
import com.koresuniku.wishmaster_v4.core.dagger.module.dashboard_scope.DashboardPresenterModule
import com.koresuniku.wishmaster_v4.core.dagger.module.RxModule
import com.koresuniku.wishmaster_v4.core.dagger.module.SearchModule
import com.koresuniku.wishmaster_v4.core.dashboard.interactor.DashboardDatabaseInteractorI
import com.koresuniku.wishmaster_v4.core.dashboard.interactor.DashboardNetworkInteractorI
import com.koresuniku.wishmaster_v4.core.dashboard.interactor.DashboardSearchInteractorI
import com.koresuniku.wishmaster_v4.core.dashboard.interactor.DashboardSharedPreferencesInteractorI
import com.koresuniku.wishmaster_v4.core.dashboard.presenter.DashboardPresenterI
import dagger.Component


@ForDashboardPresenter
@Component(
        dependencies = [ApplicationComponent::class],
        modules = [DashboardPresenterModule::class, RxModule::class, BoardsModule::class, SearchModule::class])
interface DashboardPresenterComponent {

    fun injector(): IWishmasterDaggerInjector
    fun dashboardNetworkInteractor(): DashboardNetworkInteractorI
    fun dashboardDatabaseInteractor(): DashboardDatabaseInteractorI
    fun dashboardSearchInteractor(): DashboardSearchInteractorI
    fun dashboardSharedPreferencesInteractor(): DashboardSharedPreferencesInteractorI

    fun inject(dashboardPresenter: DashboardPresenterI)

}