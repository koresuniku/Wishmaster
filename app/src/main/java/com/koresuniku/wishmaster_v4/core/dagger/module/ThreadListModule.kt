package com.koresuniku.wishmaster_v4.core.dagger.module

import com.koresuniku.wishmaster_v4.core.dagger.scope.ForDashboardView
import com.koresuniku.wishmaster_v4.core.dagger.IWishmasterInjector
import com.koresuniku.wishmaster_v4.core.thread_list.ThreadListPresenter
import dagger.Module
import dagger.Provides

/**
 * Created by koresuniku on 01.01.18.
 */

@Module
class ThreadListModule {

    @Provides
    @ForDashboardView
    fun provideThreadListPresenter(injector: IWishmasterInjector): ThreadListPresenter = ThreadListPresenter(injector)
}