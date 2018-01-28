package com.koresuniku.wishmaster_v4.core.dashboard

import com.koresuniku.wishmaster_v4.core.base.IActivityMvpView

/**
 * Created by koresuniku on 03.10.17.
 */

interface DashboardView : IActivityMvpView, DashboardSearchView {
    fun showLoading()
}