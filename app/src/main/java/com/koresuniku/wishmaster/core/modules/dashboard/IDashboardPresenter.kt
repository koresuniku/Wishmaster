package com.koresuniku.wishmaster.core.modules.dashboard

import com.koresuniku.wishmaster.core.base.mvp.IMvpPresenter
import com.koresuniku.wishmaster.core.data.model.boards.BoardModel


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