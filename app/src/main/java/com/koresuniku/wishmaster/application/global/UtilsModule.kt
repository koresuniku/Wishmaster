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

package com.koresuniku.wishmaster.application.global

import com.koresuniku.wishmaster.ui.utils.DeviceUtils
import com.koresuniku.wishmaster.ui.utils.UiUtils
import com.koresuniku.wishmaster.ui.utils.ViewUtils
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by koresuniku on 02.02.18.
 */

@Module
class UtilsModule {

    @Provides
    @Singleton
    fun provideTextUtils(): WMTextUtils = WMTextUtils()

    @Provides
    @Singleton
    fun provideImageUtils(uiUtils: UiUtils,
                          textUtils: WMTextUtils,
                          uiParams: UiParams,
                          deviceUtils: DeviceUtils): WMImageUtils {
        return WMImageUtils(uiUtils, textUtils, uiParams, deviceUtils)
    }

    @Provides
    @Singleton
    fun provideDeviceUtils(): DeviceUtils = DeviceUtils()

    @Provides
    @Singleton
    fun provideUiUtils(deviceUtils: DeviceUtils): UiUtils = UiUtils(deviceUtils)

    @Provides
    @Singleton
    fun provideViewUtils(deviceUtils: DeviceUtils): ViewUtils = ViewUtils(deviceUtils)

    @Provides
    @Singleton
    fun provideAnimationUtils(): WMAnimationUtils = WMAnimationUtils()
}