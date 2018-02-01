package com.koresuniku.wishmaster_v4.core.dashboard

import com.koresuniku.wishmaster_v4.core.base.mvp.MvpPresenter
import com.koresuniku.wishmaster_v4.core.base.mvp.MvpView
import com.koresuniku.wishmaster_v4.core.data.boards.BoardListsObject

/**
 * Created by koresuniku on 13.11.17.
 */

interface BoardListView<P> : MvpView<P> {
    fun onBoardListsObjectReceived(boardListsObject: BoardListsObject)
    fun onBoardFavourabilityChanged(boardId: String, newFavouritePosition: Int)
}