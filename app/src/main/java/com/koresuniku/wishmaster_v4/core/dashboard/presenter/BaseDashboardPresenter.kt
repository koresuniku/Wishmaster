package com.koresuniku.wishmaster_v4.core.dashboard.presenter

import com.koresuniku.wishmaster_v4.core.base.rx.BaseRxPresenter
import com.koresuniku.wishmaster_v4.core.dashboard.interactor.DashboardDatabaseInteractor
import com.koresuniku.wishmaster_v4.core.dashboard.interactor.DashboardNetworkInteractor
import com.koresuniku.wishmaster_v4.core.dashboard.interactor.DashboardSearchInteractor
import com.koresuniku.wishmaster_v4.core.dashboard.interactor.DashboardSharedPreferencesInteractor
import com.koresuniku.wishmaster_v4.core.dashboard.view.BoardListView
import com.koresuniku.wishmaster_v4.core.dashboard.view.DashboardView
import com.koresuniku.wishmaster_v4.core.dashboard.view.FavouriteBoardsView
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