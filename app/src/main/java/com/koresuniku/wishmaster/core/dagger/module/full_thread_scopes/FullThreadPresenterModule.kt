package com.koresuniku.wishmaster.core.dagger.module.full_thread_scopes

import android.content.Context
import com.koresuniku.wishmaster.application.singletones.UiParams
import com.koresuniku.wishmaster.core.dagger.scope.ForFullThreadPresenter
import com.koresuniku.wishmaster.core.modules.full_thread.FullThreadAdapterViewInteractor
import com.koresuniku.wishmaster.core.modules.full_thread.FullThreadNetworkInteractor
import com.koresuniku.wishmaster.core.network.client.RetrofitHolder
import com.koresuniku.wishmaster.core.network.full_thread_api.FullThreadApiService
import com.koresuniku.wishmaster.core.utils.images.WishmasterImageUtils
import com.koresuniku.wishmaster.core.utils.text.WishmasterTextUtils
import com.koresuniku.wishmaster.ui.utils.ViewUtils
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
    fun provideFullThreadNetworkInterator(fullThreadApiService: FullThreadApiService,
                                          compositeDisposable: CompositeDisposable): FullThreadNetworkInteractor {
        return FullThreadNetworkInteractor(fullThreadApiService, compositeDisposable)
    }

    @Provides
    @ForFullThreadPresenter
    fun provideFullThreadAdapterViewInteractor(compositeDisposable: CompositeDisposable,
                                               context: Context,
                                               uiParams: UiParams,
                                               retrofitHolder: RetrofitHolder,
                                               imageUtils: WishmasterImageUtils,
                                               textUtils: WishmasterTextUtils,
                                               viewUtils: ViewUtils):
            FullThreadAdapterViewInteractor {
        return FullThreadAdapterViewInteractor(
                compositeDisposable, context, uiParams, retrofitHolder, imageUtils, textUtils, viewUtils)
    }
}