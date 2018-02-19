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