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

package com.koresuniku.wishmaster.application.preferences

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import com.google.firebase.messaging.FirebaseMessaging
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.application.global.CommonParams
import com.koresuniku.wishmaster.application.global.UiParams
import com.koresuniku.wishmaster.application.utils.FirebaseKeystore
import com.koresuniku.wishmaster.core.data.network.client.RetrofitHolder
import com.koresuniku.wishmaster.ui.utils.DeviceUtils
import com.koresuniku.wishmaster.ui.utils.UiUtils
import com.koresuniku.wishmaster.ui.utils.ViewUtils
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.functions.Function7
import io.reactivex.schedulers.Schedulers
import org.acra.ACRA

/**
 * Created by koresuniku on 14.01.18.
 */

class SharedPreferencesHelper : ISharedPreferencesHelper {

    override fun onApplicationCreate(context: Context,
                                     sharedPreferencesStorage: SharedPreferencesStorage,
                                     retrofitHolder: RetrofitHolder,
                                     uiParams: UiParams,
                                     commonParams: CommonParams,
                                     uiUtils: UiUtils,
                                     viewUtils: ViewUtils,
                                     deviceUtils: DeviceUtils) {
        setACRA(sharedPreferencesStorage)
        setCacheSize(sharedPreferencesStorage, commonParams)
        setDefaultImageWidth(context, sharedPreferencesStorage, uiParams, uiUtils, deviceUtils)
        setRetrofitBaseUrl(sharedPreferencesStorage, retrofitHolder)
        setShortInfoHeight(context, sharedPreferencesStorage, uiParams, viewUtils)
        setMaxLines(sharedPreferencesStorage, uiParams)
        setCommentTextSize(sharedPreferencesStorage, uiParams)
        setCommentTextPaint(context, uiParams)
        setToolbarHeight(context, uiParams)
        setFirebase(context, sharedPreferencesStorage)
    }

    private fun setACRA(sharedPreferencesStorage: SharedPreferencesStorage) {
        sharedPreferencesStorage.readBoolean(
                SharedPreferencesKeystore.ENABLE_SEND_REPORTS_KEY,
                SharedPreferencesKeystore.ENABLE_SEND_REPORTS_DEFAULT)
                .subscribeOn(Schedulers.io())
                .subscribe(ACRA.getErrorReporter()::setEnabled)
    }

    private fun setCacheSize(sharedPreferencesStorage: SharedPreferencesStorage,
                             commonParams: CommonParams) {
        sharedPreferencesStorage.readInt(
                SharedPreferencesKeystore.CACHE_SIZE_KEY,
                SharedPreferencesKeystore.CACHE_SIZE_DEFAULT)
                .subscribeOn(Schedulers.io())
                .subscribe({ commonParams.cacheSize = it}, { it.printStackTrace() })
    }

    private fun setDefaultImageWidth(context: Context,
                                     sharedPreferencesStorage: ISharedPreferencesStorage,
                                     uiParams: UiParams,
                                     uiUtils: UiUtils,
                                     deviceUtils: DeviceUtils) {
        readDefaultImageWidthDependentValues(sharedPreferencesStorage)
                .subscribeOn(Schedulers.io())
                .subscribe( { values ->
                    if (values.imageWidthDp == SharedPreferencesKeystore.DEFAULT_IMAGE_WIDTH_IN_DP_DEFAULT) {
                        val newImageWidth = uiUtils.getDefaultImageWidthInDp(
                                deviceUtils.getActualDisplayWidthInPx(context), context)
                        uiParams.imageWidthDp = newImageWidth
                        uiParams.commentMarginWidth =
                                uiUtils.convertDpToPixel(newImageWidth.toFloat()).toInt() +
                                context.resources.getDimension(R.dimen.thread_post_side_margin_default).toInt()
                        if (sharedPreferencesStorage.writeIntSameThread(
                                        SharedPreferencesKeystore.DEFAULT_IMAGE_WIDTH_IN_DP_KEY, newImageWidth)) {
                            computeThreadPostItemWidth(context, sharedPreferencesStorage, uiParams, uiUtils, deviceUtils)
                                    .subscribeOn(Schedulers.computation())
                                    .subscribe()
                        }
                    } else if (values.threadPostItemHorizontalWidth == SharedPreferencesKeystore.THREAD_POST_ITEM_WIDTH_DEFAULT
                            || values.threadPostItemVerticalWidth == SharedPreferencesKeystore.THREAD_POST_ITEM_WIDTH_DEFAULT
                            || values.threadPostItemSingleImageHorizontalWidth == SharedPreferencesKeystore.THREAD_POST_ITEM_WIDTH_SINGLE_IMAGE_DEFAULT
                            || values.threadPostItemSingleImageVerticalWidth == SharedPreferencesKeystore.THREAD_POST_ITEM_WIDTH_SINGLE_IMAGE_DEFAULT) {
                        computeThreadPostItemWidth(context, sharedPreferencesStorage, uiParams, uiUtils, deviceUtils)
                                .subscribeOn(Schedulers.computation())
                                .subscribe()
                    } else {
                        uiParams.imageWidthDp = values.imageWidthDp
                        uiParams.minImageHeightDp = values.minImageHeightDp
                        uiParams.maxImageHeightDp = values.maxImageHeightDp
                        uiParams.threadPostItemHorizontalWidth = values.threadPostItemHorizontalWidth
                        uiParams.threadPostItemVerticalWidth = values.threadPostItemVerticalWidth
                        uiParams.threadPostItemSingleImageHorizontalWidth = values.threadPostItemSingleImageHorizontalWidth
                        uiParams.threadPostItemSingleImageVerticalWidth = values.threadPostItemSingleImageVerticalWidth
                        uiParams.commentMarginWidth =
                                uiUtils.convertDpToPixel(values.imageWidthDp.toFloat()).toInt() +
                                context.resources.getDimension(R.dimen.thread_post_side_margin_default).toInt()
                    }
                })

    }

    private fun setToolbarHeight(context: Context, uiParams: UiParams) {
        uiParams.toolbarHeight = context.resources.getDimension(R.dimen.app_bar_height).toInt()
    }

    private fun readDefaultImageWidthDependentValues(
            sharedPreferencesStorage: ISharedPreferencesStorage) :
            Single<UiParams> {
        return Single.zip(
                readDefaultImageWidth(sharedPreferencesStorage),
                readMinImageHeight(sharedPreferencesStorage),
                readMaxImageHeight(sharedPreferencesStorage),
                readThreadPostItemHorizontalWidth(sharedPreferencesStorage),
                readThreadPostItemVerticalWidth(sharedPreferencesStorage),
                readThreadPostItemSingleImageHorizontalWidth(sharedPreferencesStorage),
                readThreadPostItemSingleImageVerticalWidth(sharedPreferencesStorage),
                Function7({ imageWidth: Int, mih: Int, Mih: Int, hw: Int, vw: Int, sihw: Int, sivw: Int -> kotlin.run {
                    val params = UiParams()
                    params.imageWidthDp = imageWidth
                    params.minImageHeightDp = mih
                    params.maxImageHeightDp = Mih
                    params.threadPostItemHorizontalWidth = hw
                    params.threadPostItemVerticalWidth = vw
                    params.threadPostItemSingleImageHorizontalWidth = sihw
                    params.threadPostItemSingleImageVerticalWidth = sivw
                     params }
                }))
    }

    private fun setRetrofitBaseUrl(sharedPreferencesStorage: ISharedPreferencesStorage,
                                   retrofitHolder: RetrofitHolder) {
        sharedPreferencesStorage.readString(
                SharedPreferencesKeystore.BASE_URL_KEY,
                SharedPreferencesKeystore.BASE_URL_DEFAULT)
                .subscribeOn(Schedulers.io())
                .filter { value -> value != SharedPreferencesKeystore.BASE_URL_DEFAULT }
                .subscribe(retrofitHolder::changeDvachBaseUrl)
    }

    private fun setShortInfoHeight(context: Context,
                                   sharedPreferencesStorage: ISharedPreferencesStorage,
                                   uiParams: UiParams,
                                   viewUtils: ViewUtils) {
        sharedPreferencesStorage.readInt(
                SharedPreferencesKeystore.THREAD_POST_ITEM_SHORT_INFO_HEIGHT_IN_PX_KEY,
                SharedPreferencesKeystore.THREAD_POST_ITEM_SHORT_INFO_HEIGHT_DEFAULT)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (it == SharedPreferencesKeystore.THREAD_POST_ITEM_SHORT_INFO_HEIGHT_DEFAULT) {
                        val height = computeShortInfoHeight(context, viewUtils)
                        Log.d("SPH", "height $height")
                        sharedPreferencesStorage.writeIntBackground(
                                SharedPreferencesKeystore.THREAD_POST_ITEM_SHORT_INFO_HEIGHT_IN_PX_KEY,
                                height)
                        uiParams.threadPostItemShortInfoHeight = height
                    } else {
                        Log.d("SPH", "height $it")
                        uiParams.threadPostItemShortInfoHeight = it
                    }
                }, { it.printStackTrace() })
    }

    private fun setMaxLines(sharedPreferencesStorage: ISharedPreferencesStorage,
                            uiParams: UiParams) {
        sharedPreferencesStorage.readInt(
                SharedPreferencesKeystore.COMMENT_MAX_MAX_LINES_KEY,
                SharedPreferencesKeystore.COMMENT_MAX_MAX_LINES_DEFAULT)
                .subscribeOn(Schedulers.io())
                .subscribe(
                        { uiParams.commentMaxLines = it },
                        { it.printStackTrace() })
    }

    private fun setCommentTextPaint(context: Context, uiParams: UiParams) {
        val commentLayout = LayoutInflater
                .from(context)
                .inflate(R.layout.comment_layout, null, false)
        val commentTextView = commentLayout.findViewById<TextView>(R.id.comment)
        uiParams.commentTextPaint = commentTextView.paint
    }

    private fun setCommentTextSize(sharedPreferencesStorage: ISharedPreferencesStorage,
                                   uiParams: UiParams) {
        sharedPreferencesStorage.readInt(
                SharedPreferencesKeystore.COMMENT_TEXT_SIZE_KEY,
                SharedPreferencesKeystore.COMMENT_TEXT_SIZE_DEFAULT)
                .subscribeOn(Schedulers.io())
                .subscribe(
                        { uiParams.commentTextSize = it },
                        { it.printStackTrace() })
    }

    private fun computeShortInfoHeight(context: Context, viewUtils: ViewUtils): Int {
        val testImageLayout = LayoutInflater
                .from(context)
                .inflate(R.layout.image_layout, null, false)
        val testShortInfoTextView = testImageLayout.findViewById<TextView>(R.id.summary)
        viewUtils.measureView(testShortInfoTextView)
        return testShortInfoTextView.measuredHeight
    }

    private fun computeThreadPostItemWidth(context: Context,
                                           sharedPreferencesStorage: ISharedPreferencesStorage,
                                           UIParams: UiParams,
                                           uiUtils: UiUtils,
                                           deviceUtils: DeviceUtils): Completable {
        return Completable.create({ e -> kotlin.run {
            var displayWidth = deviceUtils.getDisplayWidth(context)
            var displayHeight = deviceUtils.getDisplayHeight(context)

            if (displayWidth < displayHeight) {
                val tempWidth = displayWidth
                displayWidth = displayHeight
                displayHeight = tempWidth
            }

            val sideMargin = context.resources.getDimension(R.dimen.thread_post_side_margin_default).toInt()
            val threadPostItemHorizontalWidth = displayWidth - sideMargin * 2
            val threadPostItemVerticalWidth = displayHeight - sideMargin * 2

            sharedPreferencesStorage.readInt(
                    SharedPreferencesKeystore.DEFAULT_IMAGE_WIDTH_IN_DP_KEY,
                    SharedPreferencesKeystore.DEFAULT_IMAGE_WIDTH_IN_DP_DEFAULT)
                    .subscribeOn(Schedulers.io())
                    .map({ uiUtils.convertDpToPixel(it.toFloat()).toInt() })
                    .subscribe({ value ->
                        val threadPostItemSingleImageHorizontal = threadPostItemHorizontalWidth - value - sideMargin
                        val threadPostItemSingleImageVertical = threadPostItemVerticalWidth - value - sideMargin

                        sharedPreferencesStorage.writeIntBackground(
                                SharedPreferencesKeystore.THREAD_POST_ITEM_WIDTH_HORIZONTAL_IN_PX_KEY,
                                threadPostItemHorizontalWidth)
                        sharedPreferencesStorage.writeIntBackground(
                                SharedPreferencesKeystore.THREAD_POST_ITEM_WIDTH_VERTICAL_IN_PX_KEY,
                                threadPostItemVerticalWidth)
                        sharedPreferencesStorage.writeIntBackground(
                                SharedPreferencesKeystore.THREAD_POST_ITEM_WIDTH_SINGLE_IMAGE_HORIZONTAL_IN_PX_KEY,
                                threadPostItemSingleImageHorizontal)
                        sharedPreferencesStorage.writeIntBackground(
                                SharedPreferencesKeystore.THREAD_POST_ITEM_WIDTH_SINGLE_IMAGE_VERTICAL_IN_PX_KEY,
                                threadPostItemSingleImageVertical)

                        UIParams.threadPostItemHorizontalWidth = threadPostItemHorizontalWidth
                        UIParams.threadPostItemVerticalWidth = threadPostItemVerticalWidth
                        UIParams.threadPostItemSingleImageHorizontalWidth = threadPostItemSingleImageHorizontal
                        UIParams.threadPostItemSingleImageVerticalWidth = threadPostItemSingleImageVertical

                        e.onComplete()
                    })
        }})
    }

    private fun setFirebase(context: Context, sharedPreferencesStorage: ISharedPreferencesStorage) {
        val switchSingle = sharedPreferencesStorage.readBoolean(
                context.getString(R.string.switch_new_version_notif_key),
                context.resources.getBoolean(R.bool.switch_new_version_notif_default))
        switchSingle
                .subscribeOn(Schedulers.io())
                .subscribe({ if (it)
                    FirebaseMessaging.getInstance().subscribeToTopic(FirebaseKeystore.NEW_VERSION_TOPIC)
                }, { it.printStackTrace() })
    }

    private fun readDefaultImageWidth(sharedPreferencesStorage: ISharedPreferencesStorage): Single<Int> {
        return Single.create({ e ->
            sharedPreferencesStorage.readInt(
                    SharedPreferencesKeystore.DEFAULT_IMAGE_WIDTH_IN_DP_KEY,
                    SharedPreferencesKeystore.DEFAULT_IMAGE_WIDTH_IN_DP_DEFAULT)
                    .observeOn(Schedulers.io())
                    .subscribe(e::onSuccess)
        })
    }

    private fun readMinImageHeight(sharedPreferencesStorage: ISharedPreferencesStorage): Single<Int> {
        return Single.create({ e ->
            sharedPreferencesStorage.readInt(
                    SharedPreferencesKeystore.MINIMUM_IMAGE_HEIGHT_IN_DP_KEY,
                    SharedPreferencesKeystore.MINIMUM_IMAGE_HEIGHT_IN_DP_DEFAULT)
                    .observeOn(Schedulers.io())
                    .subscribe(e::onSuccess)
        })
    }

    private fun readMaxImageHeight(sharedPreferencesStorage: ISharedPreferencesStorage): Single<Int> {
        return Single.create({ e ->
            sharedPreferencesStorage.readInt(
                    SharedPreferencesKeystore.MAXIMUM_IMAGE_HEIGHT_IN_DP_KEY,
                    SharedPreferencesKeystore.MAXIMUM_IMAGE_HEIGHT_IN_DP_DEFAULT)
                    .observeOn(Schedulers.io())
                    .subscribe(e::onSuccess)
        })
    }
    private fun readThreadPostItemHorizontalWidth(sharedPreferencesStorage: ISharedPreferencesStorage): Single<Int> {
        return Single.create({ e ->
            sharedPreferencesStorage.readInt(
                    SharedPreferencesKeystore.THREAD_POST_ITEM_WIDTH_HORIZONTAL_IN_PX_KEY,
                    SharedPreferencesKeystore.THREAD_POST_ITEM_WIDTH_DEFAULT)
                    .observeOn(Schedulers.io())
                    .subscribe(e::onSuccess)
        })
    }

    private fun readThreadPostItemVerticalWidth(sharedPreferencesStorage: ISharedPreferencesStorage): Single<Int> {
        return Single.create({ e ->
            sharedPreferencesStorage.readInt(
                    SharedPreferencesKeystore.THREAD_POST_ITEM_WIDTH_VERTICAL_IN_PX_KEY,
                    SharedPreferencesKeystore.THREAD_POST_ITEM_WIDTH_DEFAULT)
                    .observeOn(Schedulers.io())
                    .subscribe(e::onSuccess)
        })
    }

    private fun readThreadPostItemSingleImageHorizontalWidth(sharedPreferencesStorage: ISharedPreferencesStorage): Single<Int> {
        return Single.create({ e ->
            sharedPreferencesStorage.readInt(
                    SharedPreferencesKeystore.THREAD_POST_ITEM_WIDTH_SINGLE_IMAGE_HORIZONTAL_IN_PX_KEY,
                    SharedPreferencesKeystore.THREAD_POST_ITEM_WIDTH_SINGLE_IMAGE_DEFAULT)
                    .observeOn(Schedulers.io())
                    .subscribe(e::onSuccess)
        })
    }

    private fun readThreadPostItemSingleImageVerticalWidth(sharedPreferencesStorage: ISharedPreferencesStorage): Single<Int> {
        return Single.create({ e ->
            sharedPreferencesStorage.readInt(
                    SharedPreferencesKeystore.THREAD_POST_ITEM_WIDTH_SINGLE_IMAGE_VERTICAL_IN_PX_KEY,
                    SharedPreferencesKeystore.THREAD_POST_ITEM_WIDTH_SINGLE_IMAGE_DEFAULT)
                    .observeOn(Schedulers.io())
                    .subscribe(e::onSuccess)
        })
    }
}