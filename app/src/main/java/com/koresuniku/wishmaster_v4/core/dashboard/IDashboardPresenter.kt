package com.koresuniku.wishmaster_v4.core.dashboard

import com.koresuniku.wishmaster_v4.core.base.mvp.MvpPresenter
import com.koresuniku.wishmaster_v4.core.data.boards.BoardListData
import com.koresuniku.wishmaster_v4.core.data.boards.BoardModel
import io.reactivex.Single


interface IDashboardPresenter : MvpPresenter<DashboardView> {

    fun loadBoards()
    fun shouldLaunchThreadListActivity(boardId: String)
    fun processSearchInput(input: String)
    fun reorderFavouriteBoardList(boardList: List<BoardModel>)
    fun loadFavouriteBoardList()
    fun switchBoardFavourability(boardId: String)
    fun getDashboardFavouriteTabPosition()

    fun bindDashboardBoardListView(dashboardBoardListView: BoardListView)
    fun bindFavouriteBoardsView(favouriteBoardsView: FavouriteBoardsView)

    fun unbindDashboardBoardListView()
    fun unbindFavouriteBoardsView()
}