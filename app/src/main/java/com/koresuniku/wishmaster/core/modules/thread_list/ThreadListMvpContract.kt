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

package com.koresuniku.wishmaster.core.modules.thread_list

import android.text.Spanned
import com.koresuniku.wishmaster.application.notifier.OnOrientationChangedListener
import com.koresuniku.wishmaster.core.base.IAdapterViewInteractor
import com.koresuniku.wishmaster.core.base.IMvpDataPresenter
import com.koresuniku.wishmaster.core.base.IMvpView
import com.koresuniku.wishmaster.core.base.INetworkInteractor
import com.koresuniku.wishmaster.core.data.model.threads.ThreadListData
import com.koresuniku.wishmaster.core.modules.gallery.ImageItemData
import com.koresuniku.wishmaster.core.data.network.thread_list_api.ThreadListApiService
import com.koresuniku.wishmaster.core.data.network.thread_list_api.ThreadListJsonSchemaCatalogResponse
import com.koresuniku.wishmaster.core.data.network.thread_list_api.ThreadListJsonSchemaPageResponse
import com.koresuniku.wishmaster.core.utils.images.WMImageUtils
import com.koresuniku.wishmaster.ui.view.widget.WMGridView
import io.reactivex.Single

/**
 * Created by koresuniku on 3/5/18.
 */

object ThreadListMvpContract {

    //View
    interface IThreadListMainView : IMvpView {
        fun getBoardId(): String
        fun onThreadListReceived(boardName: String)
        fun showError(message: String?)
        fun showLoading()
        fun launchFullThread(threadNumber: String)
    }

    interface IThreadListAdapterView : IMvpView, OnOrientationChangedListener {
        val NO_IMAGES_CODE: Int
        val SINGLE_IMAGE_CODE: Int
        val MULTIPLE_IMAGES_CODE: Int
        fun onThreadListDataChanged(newThreadListData: ThreadListData)
    }

    interface IThreadItemView : IMvpView {
        var threadNumber: String

        fun adaptLayout(position: Int)
        fun setOnClickItemListener()
        fun switchSubjectVisibility(visible: Boolean)
        fun setSubject(subject: Spanned)
        fun setMaxLines(value: Int)
        fun setComment(comment: Spanned)
        fun setThreadShortInfo(info: String)
        fun setSingleImage(imageItemData: ImageItemData, url: String, imageUtils: WMImageUtils)
        fun setMultipleImages(imageItemDataList: List<ImageItemData>,
                              url: String,
                              imageUtils: WMImageUtils,
                              gridViewParams: WMGridView.GridViewParams,
                              summaryHeight: Int)
    }

    //Presenter
    interface IThreadListPresenter : IMvpDataPresenter<IThreadListMainView, ThreadListData> {
        var threadListAdapterView: IThreadListAdapterView?

        fun bindThreadListAdapterView(threadListAdapterView: IThreadListAdapterView)

        fun loadThreadList()
        fun getBoardId(): String
        fun getThreadItemType(position: Int): Int
        fun setItemViewData(threadItemView: IThreadItemView, position: Int)
        fun onThreadItemClicked(threadNumber: String)
        fun onNetworkError(t: Throwable)

        fun unbindThreadListAdapterView()
    }

    //Interactor
    interface IThreadListNetworkInteractor : INetworkInteractor<ThreadListApiService> {
        fun fetchThreadListData(boardId: String): Single<ThreadListData>
        fun loadThreadListFromCatalog(boardId: String): Single<ThreadListJsonSchemaCatalogResponse>
        fun loadThreadListFromPages(boardId: String): Single<ThreadListJsonSchemaPageResponse>
    }

    interface IThreadListAdapterViewInteractor :
            IAdapterViewInteractor<IThreadListAdapterView, IThreadItemView, ThreadListData>
}