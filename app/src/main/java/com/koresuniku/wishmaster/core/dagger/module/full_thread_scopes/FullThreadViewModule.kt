package com.koresuniku.wishmaster.core.dagger.module.full_thread_scopes

import com.koresuniku.wishmaster.application.listener.OrientationNotifier
import com.koresuniku.wishmaster.core.dagger.IWishmasterDaggerInjector
import com.koresuniku.wishmaster.core.dagger.scope.ForFullThreadView
import com.koresuniku.wishmaster.core.modules.full_thread.interactor.FullThreadNetworkInteractor
import com.koresuniku.wishmaster.core.modules.full_thread.presenter.FullThreadPresenter
import com.koresuniku.wishmaster.core.modules.full_thread.presenter.IFullThreadPresenter
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by koresuniku on 2/11/18.
 */

@Module
class FullThreadViewModule {

    @Provides
    @ForFullThreadView
    fun provideFullThreadPresenter(injector: IWishmasterDaggerInjector,
                               compositeDisposable: CompositeDisposable,
                                   networkInteractor: FullThreadNetworkInteractor,
                               orientationNotifier: OrientationNotifier): IFullThreadPresenter =
            FullThreadPresenter(injector, compositeDisposable, networkInteractor, orientationNotifier)
}