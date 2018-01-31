package com.koresuniku.wishmaster_v4.core.dagger.component

import com.koresuniku.wishmaster_v4.core.dagger.ForDashboardPresenter
import com.koresuniku.wishmaster_v4.core.dagger.module.BoardsModule
import com.koresuniku.wishmaster_v4.core.dagger.module.DashboardPresenterModule
import com.koresuniku.wishmaster_v4.core.dagger.module.RxModule
import com.koresuniku.wishmaster_v4.core.dagger.module.SearchModule
import com.koresuniku.wishmaster_v4.core.dashboard.DashboardDatabaseInteractor
import com.koresuniku.wishmaster_v4.core.dashboard.DashboardNetworkInteractor
import com.koresuniku.wishmaster_v4.core.dashboard.DashboardPresenter
import com.koresuniku.wishmaster_v4.core.dashboard.DashboardSearchInteractor
import dagger.Component


@ForDashboardPresenter
@Component(
        dependencies = [ApplicationComponent::class],
        modules = [DashboardPresenterModule::class, RxModule::class, BoardsModule::class, SearchModule::class])
interface DashboardPresenterComponent {

    fun dashboardNetworkInteractor(): DashboardNetworkInteractor
    fun dashboardDatabaseInteractor(): DashboardDatabaseInteractor
    fun dashboardSearchInteractor(): DashboardSearchInteractor

    fun inject(dashboardPresenter: DashboardPresenter)

}