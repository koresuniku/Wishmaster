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

import android.text.Spannable
import android.text.Spanned
import com.koresuniku.wishmaster.application.notifier.OnOrientationChangedListener
import com.koresuniku.wishmaster.core.base.IAdapterViewInteractor
import com.koresuniku.wishmaster.core.base.IMvpDataPresenter
import com.koresuniku.wishmaster.core.base.IMvpView
import com.koresuniku.wishmaster.core.base.INetworkInteractor
import com.koresuniku.wishmaster.core.data.model.posts.PostListData
import com.koresuniku.wishmaster.core.modules.gallery.ImageItemData
import com.koresuniku.wishmaster.core.data.network.full_thread_api.FullThreadApiService
import com.koresuniku.wishmaster.core.utils.images.WMImageUtils
import com.koresuniku.wishmaster.ui.view.widget.WMGridView
import io.reactivex.Single

/**
 * Created by koresuniku on 3/6/18.
 */

object FullThreadMvpContract {

    //View
    interface IFulThreadMainView : IMvpView {
        fun getBoardId(): String
        fun getThreadNumber(): String
        fun onPostListReceived(title: Spanned, itemCount: Int)
        fun onNewPostsReceived(oldCount: Int, newCount: Int)
        fun showError(message: String?)
        fun showNewPostsError(message: String?)
        fun showLoading()
    }

    interface IFullThreadAdapterView : IMvpView, OnOrientationChangedListener {
        val NO_IMAGES_CODE: Int
        val SINGLE_IMAGE_CODE: Int
        val MULTIPLE_IMAGES_CODE: Int
        fun onPostListDataChanged(newPostListData: PostListData)
        fun onNewPostsReceived(oldCount: Int, newCount: Int)
    }

    interface IPostItemView : IMvpView {
        fun setHeader(head: Spannable)
        fun switchAnswersVisibility(visible: Boolean)
        fun setOnClickItemListener(threadNumber: String)
        fun setAnswers(subject: Spanned)
        fun setComment(comment: Spanned)
        fun setSingleImage(imageItemData: ImageItemData, url: String, imageUtils: WMImageUtils)
        fun setMultipleImages(imageItemDataList: List<ImageItemData>,
                              url: String,
                              imageUtils: WMImageUtils,
                              gridViewParams: WMGridView.GridViewParams,
                              summaryHeight: Int)
    }

    //Presenter
    interface IFullThreadPresenter : IMvpDataPresenter<IFulThreadMainView, PostListData> {
        var fullThreadAdapterView: IFullThreadAdapterView?

        fun bindFullThreadAdapterView(fullThreadAdapterView: IFullThreadAdapterView)

        fun loadPostList()
        fun loadNewPostList()
        fun getBoardId(): String
        fun getThreadNumber(): String
        fun onNetworkError(t: Throwable)
        fun onNetworkNewPostsError(t: Throwable)
        fun getPostItemType(position: Int): Int
        fun setItemViewData(postItemView: IPostItemView, position: Int)

        fun unbindFullThreadAdapterView()
    }

    //Interactor
    interface IFullThreadNetworkInteractor : INetworkInteractor<FullThreadApiService> {
        fun fetchPostListData(boardId: String, threadNumber: String): Single<PostListData>
        fun fetchPostListDataStartingAt(position: Int, boardId: String, threadNumber: String): Single<PostListData>
    }

    interface IFullThreadAdapterViewInteractor :
            IAdapterViewInteractor<IFullThreadAdapterView, IPostItemView, PostListData>
}