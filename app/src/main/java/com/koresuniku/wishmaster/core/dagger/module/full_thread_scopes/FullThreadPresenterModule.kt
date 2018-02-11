package com.koresuniku.wishmaster.core.dagger.module.full_thread_scopes

import com.koresuniku.wishmaster.core.dagger.scope.ForFullThreadPresenter
import com.koresuniku.wishmaster.core.modules.full_thread.interactor.FullThreadNetworkInteractor
import com.koresuniku.wishmaster.core.network.full_thread_api.FullThreadApiService
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by koresuniku on 2/11/18.
 */

@Module
class FullThreadPresenterModule {

    @Provides
    @ForFullThreadPresenter
    fun provideFullThreadNetwrokInterator(fullThreadApiService: FullThreadApiService,
                                      compositeDisposable: CompositeDisposable): FullThreadNetworkInteractor {
        return FullThreadNetworkInteractor(fullThreadApiService, compositeDisposable)
    }
}