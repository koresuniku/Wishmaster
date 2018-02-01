package com.koresuniku.wishmaster_v4.core.dashboard.presenter

import com.koresuniku.wishmaster_v4.core.base.mvp.MvpPresenter
import com.koresuniku.wishmaster_v4.core.dashboard.view.BoardListView
import com.koresuniku.wishmaster_v4.core.dashboard.view.DashboardView
import com.koresuniku.wishmaster_v4.core.dashboard.view.FavouriteBoardsView
import com.koresuniku.wishmaster_v4.core.data.boards.BoardModel


interface IDashboardPresenter : MvpPresenter<DashboardView<IDashboardPresenter>> {

    fun loadBoards()
    fun shouldLaunchThreadListActivity(boardId: String)
    fun processSearchInput(input: String)
    fun reorderFavouriteBoardList(boardList: List<BoardModel>)
    fun loadFavouriteBoardList()
    fun switchBoardFavourability(boardId: String)
    fun getDashboardFavouriteTabPosition()

    fun bindDashboardBoardListView(dashboardBoardListView: BoardListView<IDashboardPresenter>)
    fun bindFavouriteBoardsView(favouriteBoardsView: FavouriteBoardsView<IDashboardPresenter>)

    fun unbindDashboardBoardListView()
    fun unbindFavouriteBoardsView()
}