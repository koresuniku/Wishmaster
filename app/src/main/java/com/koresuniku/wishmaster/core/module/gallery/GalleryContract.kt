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

import com.koresuniku.wishmaster.application.global.WMImageUtils
import com.koresuniku.wishmaster.core.base.IMvpPresenter
import com.koresuniku.wishmaster.core.base.IMvpView
import com.koresuniku.wishmaster.core.data.model.threads.File
import io.reactivex.Single

/**
 * Created by koresuniku on 3/7/18.
 */

object GalleryContract {

    //View
    interface IGalleryMainView : IMvpView {
        var isGalleryOpened: Boolean
        fun openGallery()
        fun closeGallery()
    }

    interface IGalleryItem : IMvpView {
        fun onTargetDimensionsReady(coordinates: WMImageUtils.ImageCoordinates)
    }

    //Presenter
    interface IGalleryPresenter : IMvpPresenter<IGalleryMainView> {
        var galleryState: GalleryState
        var fileList: MutableList<File>
        var fileMap: MutableMap<Int, MutableList<File>>
        fun onOpenGalleryClick(postPosition: Int, filePositionInPost: Int)
        fun onGalleryLayoutClicked()
        fun getFileGlobal(position: Int): File
        fun getFileLocal(postPosition: Int, filePositionInPost: Int): File
        fun getImageTargetCoordinates(position: Int, item: GalleryContract.IGalleryItem)
        fun matchFileGlobal(position: Int): MediaTypeMatcher.MediaType
        fun getUrl(): String
        fun resetGallery()
    }

    interface IGalleryDataProvider {
        fun provideFiles(galleryPresenter: IGalleryPresenter, position: Int)
    }

    //GalleryInteractor
    interface IGalleryInteractor : IMvpView {
        fun computeActualDimensions(file: File): Single<WMImageUtils.ImageCoordinates>
        fun matchFile(file: File): MediaTypeMatcher.MediaType
        fun getUrl(): String
    }
}