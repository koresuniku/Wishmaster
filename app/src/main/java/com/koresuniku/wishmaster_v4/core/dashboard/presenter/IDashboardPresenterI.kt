package com.koresuniku.wishmaster_v4.core.dashboard.presenter

import com.koresuniku.wishmaster_v4.core.base.mvp.IMvpPresenter
import com.koresuniku.wishmaster_v4.core.dashboard.view.BoardListViewI
import com.koresuniku.wishmaster_v4.core.dashboard.view.DashboardViewI
import com.koresuniku.wishmaster_v4.core.dashboard.view.FavouriteBoardsViewI
import com.koresuniku.wishmaster_v4.core.data.model.boards.BoardModel


interface IDashboardPresenterI : IMvpPresenter<DashboardViewI<IDashboardPresenterI>> {

    fun loadBoards()
    fun shouldLaunchThreadListActivity(boardId: String)
    fun processSearchInput(input: String)
    fun reorderFavouriteBoardList(boardList: List<BoardModel>)
    fun loadFavouriteBoardList()
    fun switchBoardFavourability(boardId: String)
    fun getDashboardFavouriteTabPosition()

    fun bindDashboardBoardListView(dashboardBoardListView: BoardListViewI<IDashboardPresenterI>)
    fun bindFavouriteBoardsView(favouriteBoardsView: FavouriteBoardsViewI<IDashboardPresenterI>)

    fun unbindDashboardBoardListView()
    fun unbindFavouriteBoardsView()
}