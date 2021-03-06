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

package com.koresuniku.wishmaster.core.module.gallery

import android.content.Context
import com.koresuniku.wishmaster.application.global.WMImageUtils
import com.koresuniku.wishmaster.core.data.model.threads.File
import com.koresuniku.wishmaster.core.data.network.client.RetrofitHolder
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by koresuniku on 3/4/18.
 */

class GalleryInteractor @Inject constructor(galleryLogicComponent: IGalleryLogicComponent):
        GalleryContract.IGalleryInteractor {

    @Inject lateinit var compositeDisposable: CompositeDisposable
    @Inject lateinit var imageUtils: WMImageUtils
    @Inject lateinit var context: Context
    @Inject lateinit var mediaTypeMatcher: MediaTypeMatcher
    @Inject lateinit var retrofitHolder: RetrofitHolder

    init {
        galleryLogicComponent.inject(this)
    }

    override fun computeActualDimensions(file: File): Single<WMImageUtils.ImageCoordinates> {
        return Single.create { e ->
            compositeDisposable.add(imageUtils
                    .computeImageCoordinates(context, file)
                    .subscribeOn(Schedulers.computation())
                    .subscribe(e::onSuccess))
        }
    }

    override fun matchFile(file: File) = mediaTypeMatcher.matchFile(file)

    override fun getUrl() = retrofitHolder.getDvachBaseUrl()
}