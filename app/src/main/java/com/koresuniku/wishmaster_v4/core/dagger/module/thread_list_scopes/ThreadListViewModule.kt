package com.koresuniku.wishmaster_v4.core.dagger.module.thread_list_scopes

import com.koresuniku.wishmaster_v4.core.dagger.IWishmasterDaggerInjector
import com.koresuniku.wishmaster_v4.core.dagger.scope.ForThreadListView
import com.koresuniku.wishmaster_v4.core.thread_list.interactor.ThreadListNetworkInteractor
import com.koresuniku.wishmaster_v4.core.thread_list.presenter.IThreadListPresenter
import com.koresuniku.wishmaster_v4.core.thread_list.presenter.ThreadListPresenter
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
                                   networkInteractor: ThreadListNetworkInteractor): IThreadListPresenter {
        return ThreadListPresenter(injector, compositeDisposable, networkInteractor)
    }
}