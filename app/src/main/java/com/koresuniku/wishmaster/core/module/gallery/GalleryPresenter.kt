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
import com.koresuniku.wishmaster.core.base.BaseMvpPresenter
import com.koresuniku.wishmaster.core.data.model.threads.File
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by koresuniku on 3/7/18.
 */

class GalleryPresenter @Inject constructor(galleryPresenterComponent: IGalleryPresenterComponent) :
        BaseMvpPresenter<GalleryContract.IGalleryMainView>(),
        GalleryContract.IGalleryPresenter {

    @Inject override lateinit var galleryState: GalleryState
    @Inject lateinit var compositeDisposable: CompositeDisposable
    @Inject lateinit var galleryInteractor: GalleryContract.IGalleryInteractor

    override lateinit var previewCoordinates: WMImageUtils.ImageCoordinates
    override lateinit var files: List<File>

    init {
        galleryPresenterComponent.inject(this)
    }

    override fun onOpenGalleryClick(postPosition: Int, filePosition: Int) {
        galleryState.currentPostPosition = postPosition
        galleryState.currentFilePosition = filePosition
    }

    override fun onGalleryLayoutClicked() {

    }

    override fun getFile(position: Int) = files[position]

    override fun getImageTargetCoordinates(position: Int, item: GalleryContract.IGalleryItem) {
        compositeDisposable.add(galleryInteractor
                .computeActualDimensions(getFile(position))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(item::onTargetDimensionsReady))
    }

    override fun matchFile(position: Int) = galleryInteractor.matchFile(files[position])

    override fun getUrl() = galleryInteractor.getUrl()
}