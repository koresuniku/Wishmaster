package com.koresuniku.wishmaster.core.dagger

import com.koresuniku.wishmaster.core.dagger.component.*


interface IWishmasterDaggerInjector {
    val daggerDashboardPresenterComponent: DaggerDashboardPresenterComponent
    val daggerDashboardViewComponent: DaggerDashboardViewComponent

    val daggerThreadListPresenterComponent: DaggerThreadListPresenterComponent
    val daggerThreadListViewComponent: DaggerThreadListViewComponent

    val daggerFullThreadPresenterComponent: DaggerFullThreadPresenterComponent
    val daggerFullThreadViewComponent: DaggerFullThreadViewComponent
}