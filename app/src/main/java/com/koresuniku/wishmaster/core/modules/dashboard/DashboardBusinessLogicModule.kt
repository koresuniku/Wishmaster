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

import com.koresuniku.wishmaster.core.dagger.IWishmasterDaggerInjector
import com.koresuniku.wishmaster.core.data.database.repository.BoardsRepository
import com.koresuniku.wishmaster.core.network.boards_api.BoardsResponseParser
import dagger.Module
import dagger.Provides


@Module
class DashboardBusinessLogicModule {

    @Provides
    @DashboardScopes.ForDashboardBusinessLogic
    fun provideDashboardNetworkInteractor(injector: IWishmasterDaggerInjector):
            DashboardMvpContract.IDashboardNetworkInteractor {
        return DashboardNetworkInteractor(injector)
    }

    @Provides
    @DashboardScopes.ForDashboardBusinessLogic
    fun provideDashboardDatabaseInteractor(injector: IWishmasterDaggerInjector):
            DashboardMvpContract.IDashboardDatabaseInteractor {
        return DashboardDatabaseInteractor(injector)
    }

    @Provides
    @DashboardScopes.ForDashboardBusinessLogic
    fun provideDashboardSearchInteractor(injector: IWishmasterDaggerInjector):
            DashboardMvpContract.IDashboardSearchInteractor {
        return DashboardSearchInteractor(injector)
    }

    @Provides
    @DashboardScopes.ForDashboardBusinessLogic
    fun provideDashboardSharedPreferencesInteractor(injector: IWishmasterDaggerInjector):
            DashboardMvpContract.IDashboardSharedPreferencesInteractor {
        return DashboardSharedPreferencesInteractor(injector)
    }

    @Provides
    @DashboardScopes.ForDashboardBusinessLogic
    fun provideBoardsRepository(): BoardsRepository = BoardsRepository()

    @Provides
    @DashboardScopes.ForDashboardBusinessLogic
    fun provideBoardsResponseParser(): BoardsResponseParser = BoardsResponseParser()
}