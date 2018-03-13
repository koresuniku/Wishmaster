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

package com.koresuniku.wishmaster.application

import com.koresuniku.wishmaster.application.DaggerApplicationComponent
import com.koresuniku.wishmaster.core.module.dashboard.DaggerDashboardLogicComponent
import com.koresuniku.wishmaster.core.module.dashboard.DaggerDashboardPresenterComponent
import com.koresuniku.wishmaster.core.module.dashboard.DaggerDashboardViewComponent
import com.koresuniku.wishmaster.core.module.full_thread.DaggerFullThreadLogicComponent
import com.koresuniku.wishmaster.core.module.full_thread.DaggerFullThreadPresenterComponent
import com.koresuniku.wishmaster.core.module.full_thread.DaggerFullThreadViewComponent
import com.koresuniku.wishmaster.core.module.settings.DaggerSettingsPresenterComponent
import com.koresuniku.wishmaster.core.module.settings.DaggerSettingsViewComponent
import com.koresuniku.wishmaster.core.module.thread_list.DaggerThreadListLogicComponent
import com.koresuniku.wishmaster.core.module.thread_list.DaggerThreadListPresenterComponent
import com.koresuniku.wishmaster.core.module.thread_list.DaggerThreadListViewComponent


interface IWishmasterDependencyInjector {
    val daggerApplicationComponent: DaggerApplicationComponent

    val daggerDashboardLogicComponent: DaggerDashboardLogicComponent
    val daggerDashboardPresenterComponent: DaggerDashboardPresenterComponent
    val daggerDashboardViewComponent: DaggerDashboardViewComponent

    val daggerThreadListLogicComponent: DaggerThreadListLogicComponent
    val daggerThreadListPresenterComponent: DaggerThreadListPresenterComponent
    val daggerThreadListViewComponent: DaggerThreadListViewComponent

    val daggerFullThreadLogicComponent: DaggerFullThreadLogicComponent
    val daggerFullThreadPresenterComponent: DaggerFullThreadPresenterComponent
    val daggerFullThreadViewComponent: DaggerFullThreadViewComponent

    val daggerSettingsPresenterComponent: DaggerSettingsPresenterComponent
    val daggerSettingsViewComponent: DaggerSettingsViewComponent
}