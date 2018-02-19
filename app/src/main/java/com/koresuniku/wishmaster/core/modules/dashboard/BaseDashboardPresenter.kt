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