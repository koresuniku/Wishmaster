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

import android.util.Log
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

    override var fileMap: MutableMap<Int, MutableList<File>> = hashMapOf()
    override var fileList: MutableList<File> = arrayListOf()

    init { galleryPresenterComponent.inject(this) }

    override fun onOpenGalleryClick(postPosition: Int, filePositionInPost: Int) {
        Log.d("GP", "requested Post: $postPosition")
        galleryState.currentPostPosition = postPosition
        galleryState.currentFilePositionInPost = filePositionInPost

        val theFileClicked = fileMap[postPosition]?.get(filePositionInPost)
        galleryState.currentFilePositionGlobal = fileList.indexOf(theFileClicked)
        galleryState.previewClickedPosition = galleryState.currentFilePositionGlobal

        Log.d("GP", "onOpenGalleryClick: ${fileMap}")

        mvpView?.openGallery()
    }

    override fun onGalleryLayoutClicked() {

    }

    override fun getFileGlobal(position: Int) = fileList[position]
    override fun getFileLocal(postPosition: Int, filePositionInPost: Int) =
            fileMap[postPosition]?.get(filePositionInPost) ?: File()

    override fun getImageTargetCoordinates(position: Int, item: GalleryContract.IGalleryItem) {
        compositeDisposable.add(galleryInteractor
                .computeActualDimensions(getFileGlobal(position))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(item::onTargetDimensionsReady))
    }

    override fun matchFileGlobal(position: Int) = galleryInteractor.matchFile(getFileGlobal(position))

    override fun getUrl() = galleryInteractor.getUrl()

    override fun resetGallery() {
        fileList = arrayListOf()
        fileMap = hashMapOf()
    }
}