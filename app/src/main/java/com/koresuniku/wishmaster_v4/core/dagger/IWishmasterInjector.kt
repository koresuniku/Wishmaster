package com.koresuniku.wishmaster_v4.core.dagger

import com.koresuniku.wishmaster_v4.core.dagger.component.DaggerDashboardPresenterComponent
import com.koresuniku.wishmaster_v4.core.dagger.component.DaggerThreadListViewComponent


interface IWishmasterInjector {
    fun getDashboardPresenterComponent(): DaggerDashboardPresenterComponent
    fun getThreadListComponent(): DaggerThreadListViewComponent
}