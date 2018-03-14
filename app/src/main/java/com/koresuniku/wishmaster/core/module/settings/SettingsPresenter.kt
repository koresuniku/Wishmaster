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

import com.koresuniku.wishmaster.core.base.BaseMvpPresenter
import com.koresuniku.wishmaster.application.IWMDependencyInjector
import javax.inject.Inject

/**
 * Created by koresuniku on 3/7/18.
 */

class SettingsPresenter @Inject constructor(injector: IWMDependencyInjector):
        BaseMvpPresenter<SettingsContract.ISettingsMainView>(),
        SettingsContract.ISettingsPresenter {

}