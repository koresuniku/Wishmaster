package com.koresuniku.wishmaster.core.modules.dashboard

import com.koresuniku.wishmaster.core.base.mvp.IMvpView

/**
 * Created by koresuniku on 04.01.18.
 */

interface DashboardSearchView<P> : IMvpView<P> {
    fun launchThreadListActivity(boardId: String)
    fun showUnknownInput()
}