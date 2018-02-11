package com.koresuniku.wishmaster.core.modules.dashboard.view

import com.koresuniku.wishmaster.core.base.mvp.IMvpView
import com.koresuniku.wishmaster.core.data.model.boards.BoardModel

/**
 * Created by koresuniku on 27.11.17.
 */

interface FavouriteBoardsView<P> : IMvpView<P> {
    fun onFavouriteBoardListChanged(boardList: List<BoardModel>)
}