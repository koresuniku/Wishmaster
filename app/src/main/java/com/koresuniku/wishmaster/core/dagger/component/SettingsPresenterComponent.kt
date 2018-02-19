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

package com.koresuniku.wishmaster.core.dagger.component

import com.koresuniku.wishmaster.core.dagger.IWishmasterDaggerInjector
import com.koresuniku.wishmaster.core.dagger.module.RxModule
import com.koresuniku.wishmaster.core.dagger.module.settings_scopes.SettingsPresenterModule
import com.koresuniku.wishmaster.core.dagger.scope.ForSettingsPresenter
import com.koresuniku.wishmaster.core.modules.settings.SettingsPresenter
import dagger.Component
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by koresuniku on 2/19/18.
 */

@ForSettingsPresenter
@Component(dependencies = [ApplicationComponent::class],
        modules = [(SettingsPresenterModule::class), (RxModule::class)])
interface SettingsPresenterComponent {

    fun injector(): IWishmasterDaggerInjector
    fun compositeDisposable(): CompositeDisposable
    fun inject(settingsPresenter: SettingsPresenter)

}