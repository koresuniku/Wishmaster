package com.koresuniku.wishmaster_v4.core.dashboard

import com.koresuniku.wishmaster_v4.core.base.mvp.MvpPresenter
import com.koresuniku.wishmaster_v4.core.base.mvp.MvpView
import com.koresuniku.wishmaster_v4.core.data.boards.BoardModel

/**
 * Created by koresuniku on 27.11.17.
 */

interface FavouriteBoardsView<P : IDashboardPresenter> : MvpView<P> {
    fun onFavouriteBoardListChanged(boardList: List<BoardModel>)
}