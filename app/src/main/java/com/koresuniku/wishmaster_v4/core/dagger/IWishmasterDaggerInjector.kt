package com.koresuniku.wishmaster_v4.core.dagger

import com.koresuniku.wishmaster_v4.core.dagger.component.DaggerDashboardPresenterComponent
import com.koresuniku.wishmaster_v4.core.dagger.component.DaggerThreadListPresenterComponent
import com.koresuniku.wishmaster_v4.core.dagger.component.DaggerThreadListViewComponent


interface IWishmasterDaggerInjector {
    fun getDashboardPresenterComponent(): DaggerDashboardPresenterComponent
    fun getThreadListViewComponent(): DaggerThreadListViewComponent
    fun getThreadListPresenterComponent(): DaggerThreadListPresenterComponent
}