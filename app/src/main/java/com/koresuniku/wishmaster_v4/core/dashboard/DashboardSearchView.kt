package com.koresuniku.wishmaster_v4.core.dashboard

import com.koresuniku.wishmaster_v4.core.base.mvp.MvpPresenter
import com.koresuniku.wishmaster_v4.core.base.mvp.MvpView

/**
 * Created by koresuniku on 04.01.18.
 */

interface DashboardSearchView<P> : MvpView<P> {
    fun launchThreadListActivity(boardId: String)
    fun showUnknownInput()
}