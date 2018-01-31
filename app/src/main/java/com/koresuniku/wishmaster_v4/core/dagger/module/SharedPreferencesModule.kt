package com.koresuniku.wishmaster_v4.core.dagger.module

import android.content.Context
import com.koresuniku.wishmaster_v4.application.ISharedPreferencesStorage
import com.koresuniku.wishmaster_v4.application.SharedPreferencesStorage
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
}