package com.koresuniku.wishmaster_v4.core.dagger.module

import com.koresuniku.wishmaster_v4.core.dagger.ForActivity
import com.koresuniku.wishmaster_v4.core.thread_list.ThreadListPresenter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by koresuniku on 01.01.18.
 */

@Module
class ThreadListModule {

    @Provides
    @ForActivity
    fun provideThreadListPresenter(): ThreadListPresenter = ThreadListPresenter()
}