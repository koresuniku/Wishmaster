package com.koresuniku.wishmaster_v4.core.dagger.module.thread_list_scope

import com.koresuniku.wishmaster_v4.core.dagger.scope.ForDashboardView
import com.koresuniku.wishmaster_v4.core.dagger.IWishmasterDaggerInjector
import com.koresuniku.wishmaster_v4.core.thread_list.ThreadListPresenterI
import dagger.Module
import dagger.Provides

/**
 * Created by koresuniku on 01.01.18.
 */

@Module
class ThreadListModule {

    @Provides
    @ForDashboardView
    fun provideThreadListPresenter(injector: IWishmasterDaggerInjector): ThreadListPresenterI = ThreadListPresenterI(injector)
}