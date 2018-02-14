package com.koresuniku.wishmaster.core.modules.full_thread.interactor

import android.content.Context
import com.koresuniku.wishmaster.application.preferences.UiParams
import com.koresuniku.wishmaster.core.base.rx.BaseRxAdapterViewInteractor
import com.koresuniku.wishmaster.core.data.model.posts.PostListData
import com.koresuniku.wishmaster.core.modules.full_thread.presenter.IFullThreadPresenter
import com.koresuniku.wishmaster.core.modules.full_thread.view.FullThreadAdapterView
import com.koresuniku.wishmaster.core.modules.full_thread.view.PostItemView
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

        //Comment
        post.comment?.let {
            if (post.files == null || post.files?.size != 1)
                compositeDisposable.add(textUtils.getCommentDefault(it, uiParams)
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
                                    { view.setSingleImage(it, retrofitHolder.getBaseUrl(), imageUtils)
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
                                                            imageItemData, retrofitHolder.getBaseUrl(),
                                                            imageUtils, it) },
                                                        { it.printStackTrace() }))
                                    }, { it.printStackTrace() }))
                }
                else -> {}
            }
        }
    }
}