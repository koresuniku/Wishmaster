package com.koresuniku.wishmaster_v4.core.dashboard.view

import com.koresuniku.wishmaster_v4.core.base.mvp.IMvpView
import com.koresuniku.wishmaster_v4.core.data.model.boards.BoardListData

/**
 * Created by koresuniku on 03.10.17.
 */

interface DashboardView<P> : DashboardSearchView<P>, IMvpView<P> {
    fun showLoading()
    fun onBoardListReceived(boardListData: BoardListData)
    fun onBoardListError(t: Throwable)
    fun onFavouriteTabPositionReceived(position: Int)
}