package com.koresuniku.wishmaster_v4.core.modules.dashboard.presenter

import com.koresuniku.wishmaster_v4.core.base.mvp.IMvpPresenter
import com.koresuniku.wishmaster_v4.core.modules.dashboard.view.BoardListView
import com.koresuniku.wishmaster_v4.core.modules.dashboard.view.DashboardView
import com.koresuniku.wishmaster_v4.core.modules.dashboard.view.FavouriteBoardsView
import com.koresuniku.wishmaster_v4.core.data.model.boards.BoardModel


interface IDashboardPresenter : IMvpPresenter<DashboardView<IDashboardPresenter>> {

    fun loadBoards()
    fun shouldLaunchThreadListActivity(boardId: String)
    fun processSearchInput(input: String)
    fun reorderFavouriteBoardList(boardList: List<BoardModel>)
    fun loadFavouriteBoardList()
    fun switchBoardFavourability(boardId: String)
    fun getDashboardFavouriteTabPosition()

    fun onNetworkError(t: Throwable)

    fun bindDashboardBoardListView(dashboardBoardListView: BoardListView<IDashboardPresenter>)
    fun bindFavouriteBoardsView(favouriteBoardsView: FavouriteBoardsView<IDashboardPresenter>)

    fun unbindDashboardBoardListView()
    fun unbindFavouriteBoardsView()
}