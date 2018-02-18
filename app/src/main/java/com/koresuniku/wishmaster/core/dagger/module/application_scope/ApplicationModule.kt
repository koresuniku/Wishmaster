package com.koresuniku.wishmaster.core.dagger.module.application_scope

import android.app.Application
import android.content.Context
import com.koresuniku.wishmaster.application.listener.NewReleaseNotifier
import com.koresuniku.wishmaster.application.listener.OrientationNotifier
import com.koresuniku.wishmaster.application.singletones.WishmasterDownloadManager
import com.koresuniku.wishmaster.application.singletones.WishmasterPermissionManager
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

    @Provides
    @Singleton
    fun provideNewReleaseNotifier(): NewReleaseNotifier = NewReleaseNotifier()

    @Provides
    @Singleton
    fun provideDownloadManager(context: Context): WishmasterDownloadManager =
            WishmasterDownloadManager(context)

    @Provides
    @Singleton
    fun providePermissionManager(): WishmasterPermissionManager = WishmasterPermissionManager()
}