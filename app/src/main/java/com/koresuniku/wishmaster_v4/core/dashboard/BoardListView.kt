package com.koresuniku.wishmaster_v4.core.dashboard

import com.koresuniku.wishmaster_v4.core.base.mvp.IMvpView
import com.koresuniku.wishmaster_v4.core.data.boards.BoardListData

/**
 * Created by koresuniku on 13.11.17.
 */

interface BoardListView : IMvpView {
    fun onBoardListReceived(boardListData: BoardListData)
    fun onFavouriteBoardPositionChanged(boardId: String, newFavouritePosition: Int)
}