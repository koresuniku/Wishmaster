package com.koresuniku.wishmaster.core.dagger.module.application_scope

import android.content.Context
import com.koresuniku.wishmaster.application.preferences.*
import com.koresuniku.wishmaster.application.singletones.CommonParams
import com.koresuniku.wishmaster.application.singletones.UiParams
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by koresuniku on 12.11.17.
 */

@Module
class SharedPreferencesModule {

    @Provides
    @Singleton
    fun provideSharedPreferencesStorage(context: Context): SharedPreferencesStorage {
        return SharedPreferencesStorage(context)
    }

    @Provides
    @Singleton
    fun provideUiParams(): UiParams = UiParams()

    @Provides
    @Singleton
    fun provideSharedPreferencesHelper(): SharedPreferencesHelper = SharedPreferencesHelper()

    @Provides
    @Singleton
    fun provideCommonParams(): CommonParams = CommonParams()
}