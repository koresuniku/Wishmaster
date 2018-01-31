package com.koresuniku.wishmaster_v4.core.dashboard

import com.koresuniku.wishmaster_v4.core.base.rx.BaseRxPresenter


abstract class BaseDashboardPresenter(protected val networkInteractor: DashboardNetworkInteractor,
                                      protected val databaseInteractor: DashboardDatabaseInteractor,
                                      protected val searchInteractor: DashboardSearchInteractor,
                                      protected val sharedPreferencesInteractor: DashboardSharedPreferencesInteractor):
        BaseRxPresenter<DashboardView>(), IDashboardPresenter {

    internal var dashboardBoardListView: BoardListView? = null
    internal var favouriteBoardsView: FavouriteBoardsView? = null

    override fun bindView(mvpView: DashboardView) {
        super.bindView(mvpView)
        networkInteractor.bindPresenter(this)
        databaseInteractor.bindPresenter(this)
        searchInteractor.bindPresenter(this)
        sharedPreferencesInteractor.bindPresenter(this)
    }

    override fun bindDashboardBoardListView(dashboardBoardListView: BoardListView) {
        this.dashboardBoardListView = dashboardBoardListView
    }

    override fun bindFavouriteBoardsView(favouriteBoardsView: FavouriteBoardsView) {
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
    }
}