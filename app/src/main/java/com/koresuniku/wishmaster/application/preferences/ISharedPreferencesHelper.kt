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

package com.koresuniku.wishmaster.application.preferences

import android.content.Context
import com.koresuniku.wishmaster.application.global.CommonParams
import com.koresuniku.wishmaster.application.global.UiParams
import com.koresuniku.wishmaster.core.data.network.client.RetrofitHolder
import com.koresuniku.wishmaster.ui.utils.DeviceUtils
import com.koresuniku.wishmaster.ui.utils.UiUtils
import com.koresuniku.wishmaster.ui.utils.ViewUtils


interface ISharedPreferencesHelper {
    fun onApplicationCreate(context: Context,
                            sharedPreferencesStorage: SharedPreferencesStorage,
                            retrofitHolder: RetrofitHolder,
                            uiParams: UiParams,
                            commonParams: CommonParams,
                            uiUtils: UiUtils,
                            viewUtils: ViewUtils,
                            deviceUtils: DeviceUtils)
}