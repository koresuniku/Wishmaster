package com.koresuniku.wishmaster_v4.core.dagger.module.application_scope

import com.koresuniku.wishmaster_v4.application.preferences.UiParams
import com.koresuniku.wishmaster_v4.core.gallery.WishmasterImageUtils
import com.koresuniku.wishmaster_v4.core.util.text.WishmasterTextUtils
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
    fun provideImageUtils(textUtils: WishmasterTextUtils, uiParams: UiParams):
            WishmasterImageUtils {
        return WishmasterImageUtils(textUtils, uiParams)
    }
}