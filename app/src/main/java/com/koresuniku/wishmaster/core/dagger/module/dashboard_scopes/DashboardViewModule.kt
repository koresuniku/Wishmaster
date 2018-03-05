/*
 * Copyright (c) 2018 koresuniku
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.koresuniku.wishmaster.core.dagger.module.dashboard_scopes

import com.koresuniku.wishmaster.core.dagger.scope.PerDashboardView
import com.koresuniku.wishmaster.core.dagger.IWishmasterDaggerInjector
import com.koresuniku.wishmaster.core.modules.dashboard.DashboardDatabaseInteractor
import com.koresuniku.wishmaster.core.modules.dashboard.DashboardNetworkInteractor
import com.koresuniku.wishmaster.core.modules.dashboard.DashboardSearchInteractor
import com.koresuniku.wishmaster.core.modules.dashboard.DashboardSharedPreferencesInteractor
import com.koresuniku.wishmaster.core.modules.dashboard.DashboardPresenter
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by koresuniku on 05.10.17.
 */

@Module
class DashboardViewModule {

    @Provides
    @PerDashboardView
    fun provideDashboardPresenter(injector: IWishmasterDaggerInjector,
                                  compositeDisposable: CompositeDisposable,
                                  dashboardNetworkInteractor: DashboardNetworkInteractor,
                                  dashboardDatabaseInteractor: DashboardDatabaseInteractor,
                                  dashboardSearchInteractor: DashboardSearchInteractor,
                                  dashboardSharedPreferencesInteractor: DashboardSharedPreferencesInteractor): IDashboardPresenter {
        return DashboardPresenter(injector, compositeDisposable, dashboardNetworkInteractor, dashboardDatabaseInteractor, dashboardSearchInteractor, dashboardSharedPreferencesInteractor)
    }
}