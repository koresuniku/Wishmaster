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

package com.koresuniku.wishmaster.core.modules.full_thread

import android.text.Html
import android.util.Log
import com.koresuniku.wishmaster.application.notifier.OnOrientationChangedListener
import com.koresuniku.wishmaster.application.notifier.OrientationNotifier
import com.koresuniku.wishmaster.core.base.BaseMvpPresenter
import com.koresuniku.wishmaster.core.dagger.IWishmasterDaggerInjector
import com.koresuniku.wishmaster.core.data.model.posts.PostListData
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by koresuniku on 2/11/18.
 */

class FullThreadPresenter @Inject constructor(private val injector: IWishmasterDaggerInjector):
        BaseMvpPresenter<FullThreadMvpContract.IFulThreadMainView>(),
        FullThreadMvpContract.IFullThreadPresenter, OnOrientationChangedListener {
    private val LOG_TAG = FullThreadPresenter::class.java.simpleName

    @Inject lateinit var compositeDisposable: CompositeDisposable
    @Inject lateinit var networkInteractor: FullThreadMvpContract.IFullThreadNetworkInteractor
    @Inject lateinit var adapterViewInteractor: FullThreadMvpContract.IFullThreadAdapterViewInteractor
    @Inject lateinit var orientationNotifier: OrientationNotifier

    override var fullThreadAdapterView: FullThreadMvpContract.IFullThreadAdapterView? = null

    override var presenterData: PostListData = PostListData.emptyData()
    //private var previewImageCoordinates: WMImageUtils.ImageCoordinates? = null

    override fun bindView(mvpView: FullThreadMvpContract.IFulThreadMainView) {
        super.bindView(mvpView)
        injector.daggerFullThreadPresenterComponent.inject(this)
        orientationNotifier.bindListener(this)
    }

    override fun bindFullThreadAdapterView(fullThreadAdapterView: FullThreadMvpContract.IFullThreadAdapterView) {
        this.fullThreadAdapterView = fullThreadAdapterView
    }

    override fun isDataLoaded() = presenterData.postList.isNotEmpty()
    override fun getDataSize() = presenterData.postList.count()

    override fun onOrientationChanged(orientation: Int) {
        fullThreadAdapterView?.onOrientationChanged(orientation)
    }

    override fun loadPostList() {
        mvpView?.showLoading()
        compositeDisposable.add(networkInteractor
                .fetchPostListData(getBoardId(), getThreadNumber())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    presenterData = it
                    fullThreadAdapterView?.onPostListDataChanged(it)
                    it.postList[0].let {
                        mvpView?.onPostListReceived(Html.fromHtml(
                                if (it.subject.isBlank()) it.comment else it.subject ),
                                presenterData.postList.size )
                    }
                }, { it.printStackTrace() }))
    }

    override fun loadNewPostList() {
        if (presenterData.postList.isEmpty()) {
            loadPostList()
        } else {
            compositeDisposable.add(networkInteractor
                    .fetchPostListDataStartingAt(
                            presenterData.postList.size - 1, getBoardId(), getThreadNumber())
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        Log.d(LOG_TAG, "new posts count: ${it.postList.size}")
                        val oldCount = presenterData.postList.size
                        if (it.postList.isNotEmpty()) {
                            presenterData.postList.addAll(it.postList)
                            fullThreadAdapterView?.onNewPostsReceived(
                                    oldCount, presenterData.postList.size)
                        }
                        mvpView?.onNewPostsReceived(oldCount, presenterData.postList.size)
                    }, { it.printStackTrace() }))
        }
    }

    override fun onNetworkError(t: Throwable) {
        compositeDisposable.add(Completable.fromCallable {}
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t.printStackTrace(); mvpView?.showError(t.message) }))
    }

    override fun onNetworkNewPostsError(t: Throwable) {
        compositeDisposable.add(Completable.fromCallable {}
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t.printStackTrace(); mvpView?.showNewPostsError(t.message) }))
    }

    override fun getBoardId() = mvpView?.getBoardId() ?: String()
    override fun getThreadNumber() = mvpView?.getThreadNumber() ?: String()

    override fun getPostItemType(position: Int): Int {
        presenterData.postList[position].files?.let {
            return when (it.size) {
                0 -> fullThreadAdapterView?.NO_IMAGES_CODE ?: -1
                1 -> fullThreadAdapterView?.SINGLE_IMAGE_CODE ?: -1
                else -> fullThreadAdapterView?.MULTIPLE_IMAGES_CODE ?: -1
            }
        }
        return fullThreadAdapterView?.NO_IMAGES_CODE ?: -1
    }

    override fun setItemViewData(postItemView: FullThreadMvpContract.IPostItemView, position: Int) {
        fullThreadAdapterView?.let {
            adapterViewInteractor.setItemViewData(
                    it, postItemView, presenterData, position, getPostItemType(position))
        }
    }

    override fun unbindFullThreadAdapterView() { this.fullThreadAdapterView = null }
    override fun unbindView() {
        super.unbindView()
        unbindFullThreadAdapterView()
        orientationNotifier.unbindListener(this)
        this.mvpView = null
    }
    //    override fun getGalleryState(): GalleryState {
//        //TODO: duplicate from thread list
//        return GalleryState()
//    }
//
//    override fun onOpenGalleryClick(itemPosition: Int, filePosition: Int) {
//        //TODO: implement
//    }
//
//    override fun onGalleryLayoutClicked() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun getFile(position: Int): File {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun getPreviewImageCoordinates() =
//            previewImageCoordinates ?: WMImageUtils.ImageCoordinates(0, 0, 0, 0)
//
//    override fun setPreviewImageCoordinates(coordinates: WMImageUtils.ImageCoordinates) {
//        this.previewImageCoordinates = coordinates
//    }
//
//    override fun getImageTargetCoordinates(position: Int, item: IGalleryItem) {
//
//    }
}