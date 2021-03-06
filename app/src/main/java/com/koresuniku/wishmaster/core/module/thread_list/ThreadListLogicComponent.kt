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

package com.koresuniku.wishmaster.core.module.thread_list

import android.content.Context
import com.koresuniku.wishmaster.application.notifier.OrientationNotifier
import com.koresuniku.wishmaster.application.IWMDependencyInjector
import com.koresuniku.wishmaster.application.ApplicationComponent
import com.koresuniku.wishmaster.application.global.RxModule
import com.koresuniku.wishmaster.core.data.network.client.RetrofitHolder
import com.koresuniku.wishmaster.application.global.WMTextUtils
import com.koresuniku.wishmaster.application.global.WMAnimationUtils
import com.koresuniku.wishmaster.core.module.gallery.*
import com.koresuniku.wishmaster.ui.utils.UiUtils
import dagger.Component
import io.reactivex.disposables.CompositeDisposable


@ThreadListScopes.ForThreadListLogic
@GalleryScopes.ForGalleryLogic
@Component (dependencies = [ApplicationComponent::class],
        modules = [(ThreadListLogicModule::class), (GalleryLogicModule::class), (RxModule::class)])
interface ThreadListLogicComponent : IGalleryLogicComponent {

    //Global singletons
    fun injector(): IWMDependencyInjector
    fun context(): Context
    fun textUtils(): WMTextUtils
    fun uiUtils(): UiUtils
    fun orientationNotifier(): OrientationNotifier
    fun compositeDisposable(): CompositeDisposable
    fun animationUtils(): WMAnimationUtils
    fun mediaTypeMatcher(): MediaTypeMatcher
    fun retrofitHolder(): RetrofitHolder

    //Interactors
    fun networkInteractor(): ThreadListContract.IThreadListNetworkInteractor
    fun adapterViewInteractor(): ThreadListContract.IThreadListAdapterViewInteractor

    fun inject(networkInteractor: ThreadListNetworkInteractor)
    fun inject(adapterViewInteractor: ThreadListAdapterViewInteractor)
}