package com.koresuniku.wishmaster_v4.core.dagger.module.application_scope

import android.content.Context
import com.koresuniku.wishmaster_v4.application.shared_preferences.*
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
    fun provideSharedPreferencesStorage(context: Context): ISharedPreferencesStorage {
        return SharedPreferencesStorage(context)
    }

    @Provides
    @Singleton
    fun provideSharedPreferencesUiParams(): UiParams = UiParams()

    @Provides
    @Singleton
    fun provideSharedPreferencesHelper(): ISharedPreferencesHelper = SharedPreferencesHelper()
}