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

package com.koresuniku.wishmaster.core.module.full_thread

import android.content.Context
import com.koresuniku.wishmaster.application.global.UiParams
import com.koresuniku.wishmaster.application.IWMDependencyInjector
import com.koresuniku.wishmaster.core.data.model.posts.PostListData
import com.koresuniku.wishmaster.core.data.network.client.RetrofitHolder
import com.koresuniku.wishmaster.application.global.WMImageUtils
import com.koresuniku.wishmaster.application.global.WMTextUtils
import com.koresuniku.wishmaster.ui.utils.ViewUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by koresuniku on 2/14/18.
 */

class FullThreadAdapterViewInteractor @Inject constructor(injector: IWMDependencyInjector):
        FullThreadContract.IFullThreadAdapterViewInteractor{

    @Inject lateinit var uiParams: UiParams
    @Inject lateinit var textUtils: WMTextUtils
    @Inject lateinit var compositeDisposable: CompositeDisposable
    @Inject lateinit var imageUtils: WMImageUtils
    @Inject lateinit var retrofitHolder: RetrofitHolder
    @Inject lateinit var context: Context
    @Inject lateinit var viewUtils: ViewUtils

    init { injector.daggerFullThreadLogicComponent.inject(this) }

    override fun setItemViewData(adapterView: FullThreadContract.IFullThreadAdapterView,
                                 itemView: FullThreadContract.IPostItemView,
                                 data: PostListData, position: Int, type: Int) {
        val post = data.postList[position]

        //Answers
        //TODO: count the answers!
        itemView.switchAnswersVisibility(false)

        //Header
        itemView.setHeader(textUtils.obtainPostHeader(post, position, context))

        //Comment
        post.comment?.let {
            if (post.files == null || post.files?.size != 1)
                compositeDisposable.add(textUtils.getCommentDefault(it)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ itemView.setComment(it) }, { it.printStackTrace() }))
        }

        //Images
        post.files?.let {
            when (type) {
                adapterView.SINGLE_IMAGE_CODE -> {
                    compositeDisposable.add(imageUtils.getImageItemData(it[0])
                            .subscribeOn(Schedulers.computation())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    { itemView.setSingleImage(it, retrofitHolder.getDvachBaseUrl(), imageUtils)
                                        compositeDisposable.add(textUtils.getCommentForSingleImageItemTemp(
                                                post.comment?:String(), uiParams, it)
                                                .subscribeOn(Schedulers.newThread())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe({ itemView.setComment(it) }, { it.printStackTrace() }))
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
                                                    itemView.setMultipleImages(
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