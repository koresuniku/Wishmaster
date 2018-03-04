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
import com.koresuniku.wishmaster.application.notifier.OrientationNotifier
import com.koresuniku.wishmaster.core.dagger.IWishmasterDaggerInjector
import com.koresuniku.wishmaster.core.data.model.threads.File
import com.koresuniku.wishmaster.core.modules.gallery.GalleryState
import com.koresuniku.wishmaster.core.modules.gallery.IGalleryItem
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by koresuniku on 2/11/18.
 */

class FullThreadPresenter @Inject constructor(private val injector: IWishmasterDaggerInjector,
                                              compositeDisposable: CompositeDisposable,
                                              networkInteractor: FullThreadNetworkInteractor,
                                              adapterViewInteractor: FullThreadAdapterViewInteractor,
                                              orientationNotifier: OrientationNotifier):
        BaseFullThreadPresenter(compositeDisposable, networkInteractor, adapterViewInteractor, orientationNotifier) {
    private val LOG_TAG = FullThreadPresenter::class.java.simpleName

    override fun bindView(mvpView: FullThreadView<IFullThreadPresenter>) {
        super.bindView(mvpView)
        injector.daggerFullThreadPresenterComponent.inject(this)
    }

    override fun loadPostList() {
        mvpView?.showLoading()
        compositeDisposable.add(networkInteractor
                .getDataFromNetwork()
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
                    .getPostListDataFromPosition(presenterData.postList.size - 1)
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

    override fun setItemViewData(postItemView: PostItemView, position: Int) {
        fullThreadAdapterView?.let {
            adapterViewInteractor.setItemViewData(it, postItemView, presenterData, position)
        }
    }

    override fun getGalleryState(): GalleryState {
        //TODO: duplicate from thread list
        return GalleryState()
    }

    override fun onOpenGalleryClick(itemPosition: Int, filePosition: Int) {
        //TODO: implement
    }

    override fun onGalleryLayoutClicked() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getFile(position: Int): File {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getImageTargetCoordinates(item: IGalleryItem) {

    }

    override fun onImageTargetCoordinatesReceived() {

    }
}