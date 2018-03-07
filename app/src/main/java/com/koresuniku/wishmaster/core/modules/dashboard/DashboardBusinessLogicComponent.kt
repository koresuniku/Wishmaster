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

package com.koresuniku.wishmaster.core.modules.dashboard

import com.koresuniku.wishmaster.application.notifier.NewReleaseNotifier
import com.koresuniku.wishmaster.application.singletones.WMDownloadManager
import com.koresuniku.wishmaster.application.singletones.WMPermissionManager
import com.koresuniku.wishmaster.core.dagger.IWishmasterDaggerInjector
import com.koresuniku.wishmaster.application.ApplicationComponent
import com.koresuniku.wishmaster.core.dagger.module.RxModule
import com.koresuniku.wishmaster.ui.anim.WishmasterAnimationUtils
import com.koresuniku.wishmaster.ui.utils.UiUtils
import com.koresuniku.wishmaster.ui.utils.ViewUtils
import dagger.Component
import io.reactivex.disposables.CompositeDisposable


@DashboardScopes.ForDashboardBusinessLogic
@Component(
        dependencies = [ApplicationComponent::class],
        modules = [DashboardBusinessLogicModule::class, RxModule::class, SearchModule::class])
interface DashboardBusinessLogicComponent {

    //Global singletons
    fun injector(): IWishmasterDaggerInjector
    fun uiUtils(): UiUtils
    fun viewUtils(): ViewUtils
    fun animationUtils(): WishmasterAnimationUtils
    fun newReleaseNotifier(): NewReleaseNotifier
    fun downloadManager(): WMDownloadManager
    fun permissionManager(): WMPermissionManager

    //Interactors
    fun dashboardNetworkInteractor(): DashboardMvpContract.IDashboardNetworkInteractor
    fun dashboardDatabaseInteractor(): DashboardMvpContract.IDashboardDatabaseInteractor
    fun dashboardSearchInteractor(): DashboardMvpContract.IDashboardSearchInteractor
    fun dashboardSharedPreferencesInteractor(): DashboardMvpContract.IDashboardSharedPreferencesInteractor
    fun compositeDisposable(): CompositeDisposable

    fun inject(networkInteractor: DashboardNetworkInteractor)
    fun inject(databaseInteractor: DashboardDatabaseInteractor)
    fun inject(sharedPreferencesInteractor: DashboardSharedPreferencesInteractor)
    fun inject(searchInteractor: DashboardSearchInteractor)
}