package com.koresuniku.wishmaster_v4.core.dashboard

import com.koresuniku.wishmaster_v4.core.base.mvp.IActivityMvpView
import com.koresuniku.wishmaster_v4.core.data.boards.BoardListData

/**
 * Created by koresuniku on 03.10.17.
 */

interface DashboardView : IActivityMvpView, DashboardSearchView {
    fun showLoading()
    fun onBoardListReceived(boardListData: BoardListData)
    fun onBoardListError(t: Throwable)
    fun onFavouriteTabPositionReceived(position: Int)
}