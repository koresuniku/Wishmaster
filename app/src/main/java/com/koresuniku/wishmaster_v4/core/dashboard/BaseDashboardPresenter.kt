package com.koresuniku.wishmaster_v4.core.dashboard

import com.koresuniku.wishmaster_v4.core.base.rx.BaseRxPresenter


abstract class BaseDashboardPresenter : BaseRxPresenter<DashboardView>(), IDashboardPresenter {

    internal var dashboardBoardListView: BoardListView? = null
    internal var favouriteBoardsView: FavouriteBoardsView? = null

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
}