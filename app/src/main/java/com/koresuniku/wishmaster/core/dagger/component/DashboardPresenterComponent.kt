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

package com.koresuniku.wishmaster.core.dagger.component

import com.koresuniku.wishmaster.application.notifier.NewReleaseNotifier
import com.koresuniku.wishmaster.application.singletones.WMDownloadManager
import com.koresuniku.wishmaster.application.singletones.WMPermissionManager
import com.koresuniku.wishmaster.core.dagger.scope.ForDashboardPresenter
import com.koresuniku.wishmaster.core.dagger.IWishmasterDaggerInjector
import com.koresuniku.wishmaster.core.dagger.module.dashboard_scopes.BoardsModule
import com.koresuniku.wishmaster.core.dagger.module.dashboard_scopes.DashboardPresenterModule
import com.koresuniku.wishmaster.core.dagger.module.RxModule
import com.koresuniku.wishmaster.core.dagger.module.SearchModule
import com.koresuniku.wishmaster.core.modules.dashboard.DashboardDatabaseInteractor
import com.koresuniku.wishmaster.core.modules.dashboard.DashboardNetworkInteractor
import com.koresuniku.wishmaster.core.modules.dashboard.DashboardSearchInteractor
import com.koresuniku.wishmaster.core.modules.dashboard.DashboardSharedPreferencesInteractor
import com.koresuniku.wishmaster.core.modules.dashboard.DashboardPresenter
import com.koresuniku.wishmaster.ui.anim.WishmasterAnimationUtils
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
    fun animationUtils(): WishmasterAnimationUtils
    fun newReleaseNotifier(): NewReleaseNotifier
    fun downloadManager(): WMDownloadManager
    fun permissionManager(): WMPermissionManager

    fun inject(dashboardPresenter: DashboardPresenter)

}