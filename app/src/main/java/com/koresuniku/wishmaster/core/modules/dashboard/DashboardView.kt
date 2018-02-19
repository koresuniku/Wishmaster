package com.koresuniku.wishmaster.core.modules.dashboard

import com.koresuniku.wishmaster.core.base.mvp.IMvpView
import com.koresuniku.wishmaster.core.data.model.boards.BoardListData

/**
 * Created by koresuniku on 03.10.17.
 */

interface DashboardView<P> : DashboardSearchView<P>, IMvpView<P> {
    fun showLoading()
    fun onBoardListReceived(boardListData: BoardListData)
    fun onBoardListError(t: Throwable)
    fun onFavouriteTabPositionReceived(position: Int)
}