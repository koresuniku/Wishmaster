package com.koresuniku.wishmaster.core.dagger.module.thread_list_scopes

import com.koresuniku.wishmaster.application.listener.OrientationNotifier
import com.koresuniku.wishmaster.core.dagger.IWishmasterDaggerInjector
import com.koresuniku.wishmaster.core.dagger.scope.ForThreadListView
import com.koresuniku.wishmaster.core.modules.thread_list.ThreadListAdapterViewInteractor
import com.koresuniku.wishmaster.core.modules.thread_list.ThreadListNetworkInteractor
import com.koresuniku.wishmaster.core.modules.thread_list.IThreadListPresenter
import com.koresuniku.wishmaster.core.modules.thread_list.ThreadListPresenter
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by koresuniku on 01.01.18.
 */

@Module
class ThreadListViewModule {

    @Provides
    @ForThreadListView
    fun provideThreadListPresenter(injector: IWishmasterDaggerInjector,
                                   compositeDisposable: CompositeDisposable,
                                   networkInteractor: ThreadListNetworkInteractor,
                                   adapterViewInteractor: ThreadListAdapterViewInteractor,
                                   orientationNotifier: OrientationNotifier): IThreadListPresenter {
        return ThreadListPresenter(
                injector, compositeDisposable, networkInteractor, adapterViewInteractor, orientationNotifier)
    }
}