package com.koresuniku.wishmaster.core.dagger.module.application_scope

import com.koresuniku.wishmaster.application.preferences.UiParams
import com.koresuniku.wishmaster.core.utils.images.WishmasterImageUtils
import com.koresuniku.wishmaster.core.utils.text.WishmasterTextUtils
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
    fun provideTextUtils(): WishmasterTextUtils = WishmasterTextUtils()

    @Provides
    @Singleton
    fun provideImageUtils(uiUtils: UiUtils,
                          textUtils: WishmasterTextUtils,
                          uiParams: UiParams): WishmasterImageUtils {
        return WishmasterImageUtils(uiUtils, textUtils, uiParams)
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
}