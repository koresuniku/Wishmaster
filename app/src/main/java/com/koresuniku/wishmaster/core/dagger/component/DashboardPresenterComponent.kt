package com.koresuniku.wishmaster.core.dagger.component

import com.koresuniku.wishmaster.core.dagger.scope.ForDashboardPresenter
import com.koresuniku.wishmaster.core.dagger.IWishmasterDaggerInjector
import com.koresuniku.wishmaster.core.dagger.module.dashboard_scopes.BoardsModule
import com.koresuniku.wishmaster.core.dagger.module.dashboard_scopes.DashboardPresenterModule
import com.koresuniku.wishmaster.core.dagger.module.RxModule
import com.koresuniku.wishmaster.core.dagger.module.SearchModule
import com.koresuniku.wishmaster.core.modules.dashboard.interactor.DashboardDatabaseInteractor
import com.koresuniku.wishmaster.core.modules.dashboard.interactor.DashboardNetworkInteractor
import com.koresuniku.wishmaster.core.modules.dashboard.interactor.DashboardSearchInteractor
import com.koresuniku.wishmaster.core.modules.dashboard.interactor.DashboardSharedPreferencesInteractor
import com.koresuniku.wishmaster.core.modules.dashboard.presenter.DashboardPresenter
import com.koresuniku.wishmaster.ui.utils.UiUtils
import com.koresuniku.wishmaster.ui.utils.ViewUtils
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
    fun uiUtils(): UiUtils
    fun viewUtils(): ViewUtils

    fun inject(dashboardPresenter: DashboardPresenter)

}