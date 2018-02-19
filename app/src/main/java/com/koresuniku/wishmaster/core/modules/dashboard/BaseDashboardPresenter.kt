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

import com.koresuniku.wishmaster.core.base.rx.BaseRxPresenter
import io.reactivex.disposables.CompositeDisposable


abstract class BaseDashboardPresenter(compositeDisposable: CompositeDisposable,
                                      protected val networkInteractor: DashboardNetworkInteractor,
                                      protected val databaseInteractor: DashboardDatabaseInteractor,
                                      protected val searchInteractor: DashboardSearchInteractor,
                                      protected val sharedPreferencesInteractor: DashboardSharedPreferencesInteractor):
        BaseRxPresenter<DashboardView<IDashboardPresenter>>(compositeDisposable),
        IDashboardPresenter {

    protected var dashboardBoardListView: BoardListView<IDashboardPresenter>? = null
    protected var favouriteBoardsView: FavouriteBoardsView<IDashboardPresenter>? = null

    override fun bindView(mvpView: DashboardView<IDashboardPresenter>) {
        super.bindView(mvpView)
        networkInteractor.bindPresenter(this)
        databaseInteractor.bindPresenter(this)
        searchInteractor.bindPresenter(this)
        sharedPreferencesInteractor.bindPresenter(this)
    }

    override fun bindDashboardBoardListView(dashboardBoardListView: BoardListView<IDashboardPresenter>) {
        this.dashboardBoardListView = dashboardBoardListView
    }

    override fun bindFavouriteBoardsView(favouriteBoardsView: FavouriteBoardsView<IDashboardPresenter>) {
        this.favouriteBoardsView = favouriteBoardsView
    }

    override fun unbindDashboardBoardListView() {
        this.dashboardBoardListView = null
    }

    override fun unbindFavouriteBoardsView() {
        this.favouriteBoardsView = null
    }

    override fun unbindView() {
        super.unbindView()
        networkInteractor.unbindPresenter()
        databaseInteractor.unbindPresenter()
        searchInteractor.unbindPresenter()
        sharedPreferencesInteractor.unbindPresenter()
        unbindDashboardBoardListView()
        unbindFavouriteBoardsView()
    }
}