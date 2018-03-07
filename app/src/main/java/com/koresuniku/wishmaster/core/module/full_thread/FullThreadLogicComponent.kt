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
import com.koresuniku.wishmaster.core.dagger.IWishmasterDaggerInjector
import com.koresuniku.wishmaster.application.ApplicationComponent
import com.koresuniku.wishmaster.core.dagger.module.RxModule
import com.koresuniku.wishmaster.application.global.WMTextUtils
import com.koresuniku.wishmaster.ui.anim.WishmasterAnimationUtils
import com.koresuniku.wishmaster.ui.utils.UiUtils
import dagger.Component
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by koresuniku on 2/11/18.
 */

@FullThreadScopes.ForFullThreadBusinessLogic
@Component (dependencies = [ApplicationComponent::class],
        modules = [(FullThreadLogicModule::class), (RxModule::class)] )
interface FullThreadLogicComponent {

    //Global singletons
    fun injector(): IWishmasterDaggerInjector
    fun textUtils(): WMTextUtils
    fun uiUtils(): UiUtils
    fun orientationNotifier(): OrientationNotifier
    fun compositeDisposable(): CompositeDisposable
    fun animationUtils(): WishmasterAnimationUtils

    //Interactors
    fun fullThreadNetworkInteractor(): FullThreadContract.IFullThreadNetworkInteractor
    fun fullThreadAdapterViewInteractor(): FullThreadContract.IFullThreadAdapterViewInteractor

    fun inject(networkInteractor: FullThreadNetworkInteractor)
    fun inject(adapterViewInteractor: FullThreadAdapterViewInteractor)
}