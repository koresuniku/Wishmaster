package com.koresuniku.wishmaster.core.modules.dashboard

import com.koresuniku.wishmaster.core.base.mvp.IMvpView
import com.koresuniku.wishmaster.core.data.model.boards.BoardListsObject

/**
 * Created by koresuniku on 13.11.17.
 */

interface BoardListView<P> : IMvpView<P> {
    fun onBoardListsObjectReceived(boardListsObject: BoardListsObject)
    fun onBoardFavourabilityChanged(boardId: String, newFavouritePosition: Int)
}