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

package com.koresuniku.wishmaster.core.modules.thread_list

import android.content.Context
import com.koresuniku.wishmaster.application.notifier.OrientationNotifier
import com.koresuniku.wishmaster.core.dagger.IWishmasterDaggerInjector
import com.koresuniku.wishmaster.core.dagger.component.ApplicationComponent
import com.koresuniku.wishmaster.core.dagger.module.RxModule
import com.koresuniku.wishmaster.core.modules.gallery.GalleryState
import com.koresuniku.wishmaster.core.modules.gallery.MediaTypeMatcher
import com.koresuniku.wishmaster.core.network.client.RetrofitHolder
import com.koresuniku.wishmaster.core.utils.text.WishmasterTextUtils
import com.koresuniku.wishmaster.ui.anim.WishmasterAnimationUtils
import com.koresuniku.wishmaster.ui.utils.UiUtils
import dagger.Component
import io.reactivex.disposables.CompositeDisposable


@ThreadListScopes.ForThreadListBusinessLogic
@Component (dependencies = [ApplicationComponent::class],
        modules = [(ThreadListBusinessLogicModule::class), (RxModule::class)])
interface ThreadListBusinessLogicComponent {

    //Global singletons
    fun injector(): IWishmasterDaggerInjector
    fun context(): Context
    fun textUtils(): WishmasterTextUtils
    fun uiUtils(): UiUtils
    fun orientationNotifier(): OrientationNotifier
    fun compositeDisposable(): CompositeDisposable
    fun animationUtils(): WishmasterAnimationUtils
    fun galleryState(): GalleryState
    fun mediaTypeMatcher(): MediaTypeMatcher
    fun retrofitHolder(): RetrofitHolder

    //Interactors
    fun networkInteractor(): ThreadListMvpContract.IThreadListNetworkInteractor
    fun adapterViewInteractor(): ThreadListMvpContract.IThreadListAdapterViewInteractor

    fun inject(networkInteractor: ThreadListNetworkInteractor)
    fun inject(adapterViewInteractor: ThreadListAdapterViewInteractor)

}