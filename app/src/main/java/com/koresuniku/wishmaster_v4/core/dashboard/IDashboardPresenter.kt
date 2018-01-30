package com.koresuniku.wishmaster_v4.core.dashboard

import com.koresuniku.wishmaster_v4.core.base.mvp.MvpPresenter
import com.koresuniku.wishmaster_v4.core.data.boards.BoardListData
import io.reactivex.Single


interface IDashboardPresenter : MvpPresenter<DashboardView> {

    fun loadBoards(): Single<BoardListData>
    fun shouldLaunchThreadListActivity(boardId: String)

    fun bindDashboardBoardListView(dashboardBoardListView: BoardListView)
    fun bindFavouriteBoardsView(favouriteBoardsView: FavouriteBoardsView)

    fun unbindDashboardBoardListView()
    fun unbindFavouriteBoardsView()
}