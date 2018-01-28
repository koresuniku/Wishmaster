package com.koresuniku.wishmaster_v4.core.dagger.module

import com.koresuniku.wishmaster_v4.core.base.BasePresenter
import com.koresuniku.wishmaster_v4.core.dagger.ForActivity
import com.koresuniku.wishmaster_v4.core.dashboard.DashboardPresenter
import com.koresuniku.wishmaster_v4.core.dashboard.DashboardView
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by koresuniku on 05.10.17.
 */

@Module
class DashboardModule {

    @Provides
    @ForActivity
    fun provideDashboardPresenter(): DashboardPresenter = DashboardPresenter()
}