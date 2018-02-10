package com.koresuniku.wishmaster_v4.core.dagger.module.application_scope

import android.app.Application
import com.koresuniku.wishmaster_v4.application.OrientationNotifier
import com.koresuniku.wishmaster_v4.application.WishmasterApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
* Created by koresuniku on 03.10.17.
*/

@Module
class ApplicationModule(private val application: Application) {

    @Provides
    @Singleton
    fun provideApplication() = application

    @Provides
    @Singleton
    fun provideContext() = application.applicationContext

    @Provides
    @Singleton
    fun provideOrientationNotifier(): OrientationNotifier = OrientationNotifier()
}