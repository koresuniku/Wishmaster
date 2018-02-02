package com.koresuniku.wishmaster_v4.core.dagger

import com.koresuniku.wishmaster_v4.core.dagger.component.DaggerDashboardPresenterComponent
import com.koresuniku.wishmaster_v4.core.dagger.component.DaggerDashboardViewComponent
import com.koresuniku.wishmaster_v4.core.dagger.component.DaggerThreadListPresenterComponent
import com.koresuniku.wishmaster_v4.core.dagger.component.DaggerThreadListViewComponent


interface IWishmasterDaggerInjector {
    val daggerDashboardPresenterComponent: DaggerDashboardPresenterComponent
    val daggerDashboardViewComponent: DaggerDashboardViewComponent
    val daggerThreadListViewComponent: DaggerThreadListViewComponent
    val daggerThreadListPresenterComponent: DaggerThreadListPresenterComponent
}