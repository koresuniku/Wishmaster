package com.koresuniku.wishmaster_v4.core.dagger.module

import android.app.Application
import android.content.Context
import com.koresuniku.wishmaster_v4.application.ISharedPreferencesStorage
import com.koresuniku.wishmaster_v4.application.SharedPreferencesStorage
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
    fun provideSharedPreferencesStorage(context: Context): ISharedPreferencesStorage = SharedPreferencesStorage(context)
}