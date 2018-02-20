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

package com.koresuniku.wishmaster.core.dagger.module.application_scope

import android.app.Application
import android.content.Context
import com.koresuniku.wishmaster.application.notifier.NewReleaseNotifier
import com.koresuniku.wishmaster.application.notifier.OrientationNotifier
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