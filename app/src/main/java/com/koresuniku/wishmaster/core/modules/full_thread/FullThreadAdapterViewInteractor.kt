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

import android.content.Context
import com.koresuniku.wishmaster.application.singletones.UiParams
import com.koresuniku.wishmaster.core.base.rx.BaseRxAdapterViewInteractor
import com.koresuniku.wishmaster.core.data.model.posts.PostListData
import com.koresuniku.wishmaster.core.network.client.RetrofitHolder
import com.koresuniku.wishmaster.core.utils.images.WishmasterImageUtils
import com.koresuniku.wishmaster.core.utils.text.WishmasterTextUtils
import com.koresuniku.wishmaster.ui.utils.ViewUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by koresuniku on 2/14/18.
 */

class FullThreadAdapterViewInteractor @Inject constructor(compositeDisposable: CompositeDisposable,
                                                          private val context: Context,
                                                          private val uiParams: UiParams,
                                                          private val retrofitHolder: RetrofitHolder,
                                                          private val imageUtils: WishmasterImageUtils,
                                                          private val textUtils: WishmasterTextUtils,
                                                          private val viewUtils: ViewUtils):
        BaseRxAdapterViewInteractor<
                IFullThreadPresenter,
                FullThreadAdapterView<IFullThreadPresenter>,
                PostItemView,
                PostListData>(compositeDisposable) {

    override fun setItemViewData(adapterView: FullThreadAdapterView<IFullThreadPresenter>,
                                 view: PostItemView, data: PostListData, position: Int) {
        val post = data.postList[position]

        //Answers
        //TODO: count the answers!
        view.switchAnswersVisibility(false)

        //Header
        view.setHeader(textUtils.obtainPostHeader(post, position, context))

        //Comment
        post.comment?.let {
            if (post.files == null || post.files?.size != 1)
                compositeDisposable.add(textUtils.getCommentDefault(it)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ view.setComment(it) }, { it.printStackTrace() }))
        }

        //Images
        post.files?.let {
            when (adapterView.presenter.getPostItemType(position)) {
                adapterView.SINGLE_IMAGE_CODE -> {
                    compositeDisposable.add(imageUtils.getImageItemData(it[0])
                            .subscribeOn(Schedulers.computation())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    { view.setSingleImage(it, retrofitHolder.getDvachBaseUrl(), imageUtils)
                                        compositeDisposable.add(textUtils.getCommentForSingleImageItemTemp(
                                                post.comment?:String(), uiParams, it)
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