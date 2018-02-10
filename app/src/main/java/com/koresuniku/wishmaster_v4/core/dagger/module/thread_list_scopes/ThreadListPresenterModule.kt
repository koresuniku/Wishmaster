package com.koresuniku.wishmaster_v4.core.dagger.module.thread_list_scopes

import android.content.Context
import com.koresuniku.wishmaster_v4.application.preferences.UiParams
import com.koresuniku.wishmaster_v4.core.dagger.scope.ForThreadListPresenter
import com.koresuniku.wishmaster_v4.core.gallery.WishmasterImageUtils
import com.koresuniku.wishmaster_v4.core.network.client.RetrofitHolder
import com.koresuniku.wishmaster_v4.core.network.thread_list_api.ThreadListResponseParser
import com.koresuniku.wishmaster_v4.core.network.thread_list_api.ThreadListApiService
import com.koresuniku.wishmaster_v4.core.thread_list.interactor.ThreadListAdapterViewInteractor
import com.koresuniku.wishmaster_v4.core.thread_list.interactor.ThreadListNetworkInteractor
import com.koresuniku.wishmaster_v4.core.util.text.WishmasterTextUtils
import com.koresuniku.wishmaster_v4.ui.utils.ViewUtils
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable


@Module
class ThreadListPresenterModule {

    @Provides
    @ForThreadListPresenter
    fun provideThreadListNetworkInteractor(threadListApiService: ThreadListApiService,
                                          responseParser: ThreadListResponseParser,
                                          compositeDisposable: CompositeDisposable): ThreadListNetworkInteractor {
        return ThreadListNetworkInteractor(threadListApiService, responseParser, compositeDisposable)
    }

    @Provides
    @ForThreadListPresenter
    fun provideThreadListAdapterViewInteractor(compositeDisposable: CompositeDisposable,
                                               context: Context,
                                               uiParams: UiParams,
                                               retrofitHolder: RetrofitHolder,
                                               imageUtils: WishmasterImageUtils,
                                               textUtils: WishmasterTextUtils,
                                               viewUtils: ViewUtils):
            ThreadListAdapterViewInteractor {
        return ThreadListAdapterViewInteractor(
                compositeDisposable, context, uiParams, retrofitHolder, imageUtils, textUtils, viewUtils)
    }
}