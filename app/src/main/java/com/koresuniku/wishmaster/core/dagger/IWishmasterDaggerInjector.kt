package com.koresuniku.wishmaster.core.dagger

import com.koresuniku.wishmaster.core.dagger.component.DaggerDashboardPresenterComponent
import com.koresuniku.wishmaster.core.dagger.component.DaggerDashboardViewComponent
import com.koresuniku.wishmaster.core.dagger.component.DaggerThreadListPresenterComponent
import com.koresuniku.wishmaster.core.dagger.component.DaggerThreadListViewComponent


interface IWishmasterDaggerInjector {
    val daggerDashboardPresenterComponent: DaggerDashboardPresenterComponent
    val daggerDashboardViewComponent: DaggerDashboardViewComponent
    val daggerThreadListViewComponent: DaggerThreadListViewComponent
    val daggerThreadListPresenterComponent: DaggerThreadListPresenterComponent
}