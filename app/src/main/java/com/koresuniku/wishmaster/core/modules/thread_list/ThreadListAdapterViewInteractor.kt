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

import android.content.Context
import com.koresuniku.wishmaster.application.singletones.UiParams
import com.koresuniku.wishmaster.core.base.rx.BaseRxAdapterViewInteractor
import com.koresuniku.wishmaster.core.data.model.threads.ThreadListData
import com.koresuniku.wishmaster.core.utils.images.WishmasterImageUtils
import com.koresuniku.wishmaster.core.network.client.RetrofitHolder
import com.koresuniku.wishmaster.core.utils.text.WishmasterTextUtils
import com.koresuniku.wishmaster.ui.utils.ViewUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by koresuniku on 02.02.18.
 */
class ThreadListAdapterViewInteractor @Inject constructor(compositeDisposable: CompositeDisposable,
                                                          private val context: Context,
                                                          private val uiParams: UiParams,
                                                          private val retrofitHolder: RetrofitHolder,
                                                          private val imageUtils: WishmasterImageUtils,
                                                          private val textUtils: WishmasterTextUtils,
                                                          private val viewUtils: ViewUtils):
        BaseRxAdapterViewInteractor<
                IThreadListPresenter,
                ThreadListAdapterView<IThreadListPresenter>,
                ThreadItemView,
        ThreadListData>(compositeDisposable) {

    override fun setItemViewData(adapterView: ThreadListAdapterView<IThreadListPresenter>,
                                 view: ThreadItemView,
                                 data: ThreadListData,
                                 position: Int) {
        val thread = data.getThreadList()[position]

        view.threadNumber = thread.num
        view.adaptLayout(position)
        view.setOnClickItemListener()

        //Comment
        thread.comment?.let {
            view.setMaxLines(uiParams.commentMaxLines)
            if (thread.files == null || thread.files?.size != 1)
                compositeDisposable.add(textUtils.getCommentDefault(it)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ view.setComment(it) }, { it.printStackTrace() }))
        }

        //Subject
        thread.subject?.let { view.setSubject(textUtils.getSubjectSpanned(it, data.getBoardId())) }
        view.switchSubjectVisibility(!thread.subject.isNullOrBlank() && data.getBoardId() != "b")

        //ShortInfo
        view.setThreadShortInfo(textUtils.getThreadBriefInfo(thread.postsCount, thread.filesCount))

        //Images
        thread.files?.let {
            when (adapterView.presenter.getThreadItemType(position)) {
                adapterView.SINGLE_IMAGE_CODE -> {
                    compositeDisposable.add(imageUtils.getImageItemData(it[0])
                            .subscribeOn(Schedulers.computation())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    { view.setSingleImage(it, retrofitHolder.getDvachBaseUrl(), imageUtils)
                                        compositeDisposable.add(textUtils.getCommentForSingleImageItemTemp(
                                              thread.comment?:String(), uiParams, it)
                                              .subscribeOn(Schedulers.newThread())
                                              .observeOn(AndroidSchedulers.mainThread())
                                              .subscribe({ view.setComment(it) }, { it.printStackTrace() }))
                                    }, { it.printStackTrace() }))
                }
                adapterView.MULTIPLE_IMAGES_CODE -> {
                    compositeDisposable.add(imageUtils.getImageItemData(it)
                            .subscribeOn(Schedulers.computation())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    {
                                        val imageItemData = it
                                        compositeDisposable.add(viewUtils.getGridViewHeight(
                                                context, it, it[0].dimensions.widthInPx,
                                                uiParams.threadPostItemShortInfoHeight)
                                                .subscribeOn(Schedulers.newThread())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe({
                                                    view.setMultipleImages(
                                                            imageItemData, retrofitHolder.getDvachBaseUrl(),
                                                            imageUtils, it, uiParams.threadPostItemShortInfoHeight) },
                                                        { it.printStackTrace() }))
                                    }, { it.printStackTrace() }))
                }
                else -> {}
            }
        }
    }
}