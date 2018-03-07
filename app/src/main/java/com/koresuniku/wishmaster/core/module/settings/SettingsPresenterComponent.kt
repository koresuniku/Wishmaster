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

package com.koresuniku.wishmaster.core.module.settings

import com.koresuniku.wishmaster.application.ApplicationComponent
import com.koresuniku.wishmaster.core.dagger.IWishmasterDaggerInjector
import com.koresuniku.wishmaster.core.dagger.module.RxModule
import dagger.Component
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by koresuniku on 2/19/18.
 */

@SettingsScopes.ForSettingsPresenter
@Component(dependencies = [ApplicationComponent::class],
        modules = [(SettingsPresenterModule::class), (RxModule::class)])
interface SettingsPresenterComponent {

    fun injector(): IWishmasterDaggerInjector
    fun compositeDisposable(): CompositeDisposable
}