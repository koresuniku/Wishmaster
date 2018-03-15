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

package com.koresuniku.wishmaster.core.module.full_thread

import com.koresuniku.wishmaster.application.notifier.OrientationNotifier
import com.koresuniku.wishmaster.application.IWMDependencyInjector
import com.koresuniku.wishmaster.application.ApplicationComponent
import com.koresuniku.wishmaster.application.global.RxModule
import com.koresuniku.wishmaster.application.global.WMTextUtils
import com.koresuniku.wishmaster.application.global.WMAnimationUtils
import com.koresuniku.wishmaster.core.module.gallery.GalleryLogicModule
import com.koresuniku.wishmaster.core.module.gallery.GalleryScopes
import com.koresuniku.wishmaster.core.module.gallery.IGalleryLogicComponent
import com.koresuniku.wishmaster.core.module.gallery.MediaTypeMatcher
import com.koresuniku.wishmaster.ui.utils.UiUtils
import dagger.Component
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by koresuniku on 2/11/18.
 */

@FullThreadScopes.ForFullThreadLogic
@GalleryScopes.ForGalleryLogic
@Component (dependencies = [ApplicationComponent::class],
        modules = [(FullThreadLogicModule::class), (GalleryLogicModule::class), (RxModule::class)] )
interface FullThreadLogicComponent : IGalleryLogicComponent {

    //Global singletons
    fun injector(): IWMDependencyInjector
    fun textUtils(): WMTextUtils
    fun uiUtils(): UiUtils
    fun orientationNotifier(): OrientationNotifier
    fun compositeDisposable(): CompositeDisposable
    fun animationUtils(): WMAnimationUtils
    fun mediaTypeMatcher(): MediaTypeMatcher

    //Interactors
    fun fullThreadNetworkInteractor(): FullThreadContract.IFullThreadNetworkInteractor
    fun fullThreadAdapterViewInteractor(): FullThreadContract.IFullThreadAdapterViewInteractor

    fun inject(networkInteractor: FullThreadNetworkInteractor)
    fun inject(adapterViewInteractor: FullThreadAdapterViewInteractor)
}