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

package com.koresuniku.wishmaster.core.module.dashboard

import com.koresuniku.wishmaster.application.notifier.NewReleaseNotifier
import com.koresuniku.wishmaster.application.global.WMDownloadManager
import com.koresuniku.wishmaster.application.global.WMPermissionManager
import com.koresuniku.wishmaster.application.IWMDependencyInjector
import com.koresuniku.wishmaster.application.ApplicationComponent
import com.koresuniku.wishmaster.application.global.RxModule
import com.koresuniku.wishmaster.application.global.WMAnimationUtils
import com.koresuniku.wishmaster.ui.utils.UiUtils
import com.koresuniku.wishmaster.ui.utils.ViewUtils
import dagger.Component
import io.reactivex.disposables.CompositeDisposable


@DashboardScopes.ForDashboardLogic
@Component(
        dependencies = [ApplicationComponent::class],
        modules = [DashboardLogicModule::class, RxModule::class, SearchModule::class])
interface DashboardLogicComponent {

    //Global singletons
    fun injector(): IWMDependencyInjector
    fun uiUtils(): UiUtils
    fun viewUtils(): ViewUtils
    fun animationUtils(): WMAnimationUtils
    fun newReleaseNotifier(): NewReleaseNotifier
    fun downloadManager(): WMDownloadManager
    fun permissionManager(): WMPermissionManager

    //Interactors
    fun dashboardNetworkInteractor(): DashboardContract.IDashboardNetworkInteractor
    fun dashboardDatabaseInteractor(): DashboardContract.IDashboardDatabaseInteractor
    fun dashboardSearchInteractor(): DashboardContract.IDashboardSearchInteractor
    fun dashboardSharedPreferencesInteractor(): DashboardContract.IDashboardSharedPreferencesInteractor
    fun compositeDisposable(): CompositeDisposable

    fun inject(networkInteractor: DashboardNetworkInteractor)
    fun inject(databaseInteractor: DashboardDatabaseInteractor)
    fun inject(sharedPreferencesInteractor: DashboardSharedPreferencesInteractor)
    fun inject(searchInteractor: DashboardSearchInteractor)
}