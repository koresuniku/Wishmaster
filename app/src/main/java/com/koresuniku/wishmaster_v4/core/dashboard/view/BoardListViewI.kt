package com.koresuniku.wishmaster_v4.core.dashboard.view

import com.koresuniku.wishmaster_v4.core.base.mvp.IMvpView
import com.koresuniku.wishmaster_v4.core.data.model.boards.BoardListsObject

/**
 * Created by koresuniku on 13.11.17.
 */

interface BoardListViewI<P> : IMvpView<P> {
    fun onBoardListsObjectReceived(boardListsObject: BoardListsObject)
    fun onBoardFavourabilityChanged(boardId: String, newFavouritePosition: Int)
}