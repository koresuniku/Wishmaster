package com.koresuniku.wishmaster.core.dagger.module.dashboard_scopes

import com.koresuniku.wishmaster.core.dagger.scope.ForDashboardView
import com.koresuniku.wishmaster.core.dagger.IWishmasterDaggerInjector
import com.koresuniku.wishmaster.core.modules.dashboard.interactor.DashboardDatabaseInteractor
import com.koresuniku.wishmaster.core.modules.dashboard.interactor.DashboardNetworkInteractor
import com.koresuniku.wishmaster.core.modules.dashboard.interactor.DashboardSearchInteractor
import com.koresuniku.wishmaster.core.modules.dashboard.interactor.DashboardSharedPreferencesInteractor
import com.koresuniku.wishmaster.core.modules.dashboard.presenter.DashboardPresenter
import com.koresuniku.wishmaster.core.modules.dashboard.presenter.IDashboardPresenter
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by koresuniku on 05.10.17.
 */

@Module
class DashboardViewModule {

    @Provides
    @ForDashboardView
    fun provideDashboardPresenter(injector: IWishmasterDaggerInjector,
                                  compositeDisposable: CompositeDisposable,
                                  dashboardNetworkInteractor: DashboardNetworkInteractor,
                                  dashboardDatabaseInteractor: DashboardDatabaseInteractor,
                                  dashboardSearchInteractor: DashboardSearchInteractor,
                                  dashboardSharedPreferencesInteractor: DashboardSharedPreferencesInteractor): IDashboardPresenter {
        return DashboardPresenter(injector, compositeDisposable, dashboardNetworkInteractor, dashboardDatabaseInteractor, dashboardSearchInteractor, dashboardSharedPreferencesInteractor)
    }
}