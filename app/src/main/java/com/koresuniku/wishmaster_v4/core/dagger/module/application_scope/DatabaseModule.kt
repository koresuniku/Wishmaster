package com.koresuniku.wishmaster_v4.core.dagger.module.application_scope

import android.content.Context
import com.koresuniku.wishmaster_v4.core.data.database.DatabaseHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by koresuniku on 10.11.17.
 */

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabaseHelper(context: Context) = DatabaseHelper(context)
}