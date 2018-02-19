/*
 * Copyright (c) 2018 koresuniku
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.koresuniku.wishmaster.core.dagger.module.thread_list_scopes

import android.content.Context
import com.koresuniku.wishmaster.application.singletones.UiParams
import com.koresuniku.wishmaster.core.dagger.scope.ForThreadListPresenter
import com.koresuniku.wishmaster.core.utils.images.WishmasterImageUtils
import com.koresuniku.wishmaster.core.network.client.RetrofitHolder
import com.koresuniku.wishmaster.core.network.thread_list_api.ThreadListResponseParser
import com.koresuniku.wishmaster.core.network.thread_list_api.ThreadListApiService
import com.koresuniku.wishmaster.core.modules.thread_list.ThreadListAdapterViewInteractor
import com.koresuniku.wishmaster.core.modules.thread_list.ThreadListNetworkInteractor
import com.koresuniku.wishmaster.core.utils.text.WishmasterTextUtils
import com.koresuniku.wishmaster.ui.utils.ViewUtils
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