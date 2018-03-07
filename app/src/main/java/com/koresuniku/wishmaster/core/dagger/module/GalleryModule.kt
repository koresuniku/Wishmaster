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

package com.koresuniku.wishmaster.core.dagger.module

import android.content.Context
import com.koresuniku.wishmaster.core.module.gallery.GalleryInteractor
import com.koresuniku.wishmaster.core.module.gallery.GalleryState
import com.koresuniku.wishmaster.core.module.gallery.MediaTypeMatcher
import com.koresuniku.wishmaster.application.global.WMImageUtils
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

/**
* Created by koresuniku on 3/2/18.
*/

@Module
class GalleryModule {

    @Provides
    fun provideGalleryState(): GalleryState = GalleryState()

    @Provides
    fun provideMediaTypeMatcher(): MediaTypeMatcher = MediaTypeMatcher()

    @Provides
    fun provideGalleryInteractor(compositeDisposable: CompositeDisposable,
                                 context: Context,
                                 imageUtils: WMImageUtils): GalleryInteractor =
            GalleryInteractor(compositeDisposable, context, imageUtils)
}