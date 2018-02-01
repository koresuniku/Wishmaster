package com.koresuniku.wishmaster_v4.core.dashboard.presenter

import com.koresuniku.wishmaster_v4.core.base.rx.BaseRxPresenterI
import com.koresuniku.wishmaster_v4.core.dashboard.interactor.DashboardDatabaseInteractor
import com.koresuniku.wishmaster_v4.core.dashboard.interactor.DashboardNetworkInteractor
import com.koresuniku.wishmaster_v4.core.dashboard.interactor.DashboardSearchInteractor
import com.koresuniku.wishmaster_v4.core.dashboard.interactor.DashboardSharedPreferencesInteractor
import com.koresuniku.wishmaster_v4.core.dashboard.view.BoardListViewI
import com.koresuniku.wishmaster_v4.core.dashboard.view.DashboardViewI
import com.koresuniku.wishmaster_v4.core.dashboard.view.FavouriteBoardsViewI


abstract class BaseDashboardPresenterI(protected val networkInteractor: DashboardNetworkInteractor,
                                       protected val databaseInteractor: DashboardDatabaseInteractor,
                                       protected val searchInteractor: DashboardSearchInteractor,
                                       protected val sharedPreferencesInteractor: DashboardSharedPreferencesInteractor):
        BaseRxPresenterI<DashboardViewI<IDashboardPresenterI>>(), IDashboardPresenterI {

    protected var dashboardBoardListView: BoardListViewI<IDashboardPresenterI>? = null
    protected var favouriteBoardsView: FavouriteBoardsViewI<IDashboardPresenterI>? = null

    override fun bindView(mvpView: DashboardViewI<IDashboardPresenterI>) {
        super.bindView(mvpView)
        networkInteractor.bindPresenter(this)
        databaseInteractor.bindPresenter(this)
        searchInteractor.bindPresenter(this)
        sharedPreferencesInteractor.bindPresenter(this)
    }

    override fun bindDashboardBoardListView(dashboardBoardListView: BoardListViewI<IDashboardPresenterI>) {
        this.dashboardBoardListView = dashboardBoardListView
    }

    override fun bindFavouriteBoardsView(favouriteBoardsView: FavouriteBoardsViewI<IDashboardPresenterI>) {
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