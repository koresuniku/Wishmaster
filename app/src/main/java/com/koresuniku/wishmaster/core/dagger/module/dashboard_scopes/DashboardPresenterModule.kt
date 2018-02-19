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

import com.koresuniku.wishmaster.domain.boards_api.BoardsApiService
import com.koresuniku.wishmaster.application.preferences.SharedPreferencesStorage
import com.koresuniku.wishmaster.core.dagger.scope.ForDashboardPresenter
import com.koresuniku.wishmaster.core.modules.dashboard.DashboardDatabaseInteractor
import com.koresuniku.wishmaster.core.modules.dashboard.DashboardNetworkInteractor
import com.koresuniku.wishmaster.core.modules.dashboard.DashboardSearchInteractor
import com.koresuniku.wishmaster.core.modules.dashboard.DashboardSharedPreferencesInteractor
import com.koresuniku.wishmaster.core.network.boards_api.BoardsResponseParser
import com.koresuniku.wishmaster.core.data.database.DatabaseHelper
import com.koresuniku.wishmaster.core.data.database.repository.BoardsRepository
import com.koresuniku.wishmaster.core.utils.search.SearchInputMatcher
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable


@Module
class DashboardPresenterModule {

    @Provides
    @ForDashboardPresenter
    fun provideDashboardNetworkInteractor(boardsApiService: BoardsApiService,
                                          responseParser: BoardsResponseParser,
                                          compositeDisposable: CompositeDisposable): DashboardNetworkInteractor {
        return DashboardNetworkInteractor(boardsApiService, responseParser, compositeDisposable)
    }

    @Provides
    @ForDashboardPresenter
    fun provideDashboardDatabaseInteractor(boardsRepository: BoardsRepository,
                                           databaseHelper: DatabaseHelper,
                                           compositeDisposable: CompositeDisposable): DashboardDatabaseInteractor {
        return DashboardDatabaseInteractor(boardsRepository, databaseHelper, compositeDisposable)
    }

    @Provides
    @ForDashboardPresenter
    fun provideDashboardSearchInteractor(matcher: SearchInputMatcher,
                                         compositeDisposable: CompositeDisposable): DashboardSearchInteractor {
        return DashboardSearchInteractor(matcher, compositeDisposable)
    }

    @Provides
    @ForDashboardPresenter
    fun provideDashboardSharedPreferencesInteractor(storage: SharedPreferencesStorage,
                                                    compositeDisposable: CompositeDisposable): DashboardSharedPreferencesInteractor {
        return DashboardSharedPreferencesInteractor(storage, compositeDisposable)
    }
}