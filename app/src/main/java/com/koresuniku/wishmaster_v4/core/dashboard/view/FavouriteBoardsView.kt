package com.koresuniku.wishmaster_v4.core.dashboard.view

import com.koresuniku.wishmaster_v4.core.base.mvp.IMvpView
import com.koresuniku.wishmaster_v4.core.dashboard.presenter.IDashboardPresenter
import com.koresuniku.wishmaster_v4.core.data.model.boards.BoardModel

/**
 * Created by koresuniku on 27.11.17.
 */

interface FavouriteBoardsView<P> : IMvpView<P> {
    fun onFavouriteBoardListChanged(boardList: List<BoardModel>)
}