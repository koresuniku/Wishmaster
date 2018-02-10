package com.koresuniku.wishmaster_v4.application.preferences

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import com.koresuniku.wishmaster_v4.R
import com.koresuniku.wishmaster_v4.core.network.client.RetrofitHolder
import com.koresuniku.wishmaster_v4.ui.utils.DeviceUtils
import com.koresuniku.wishmaster_v4.ui.utils.UiUtils
import com.koresuniku.wishmaster_v4.ui.utils.ViewUtils
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.functions.Function7
import io.reactivex.schedulers.Schedulers

/**
 * Created by koresuniku on 14.01.18.
 */

class SharedPreferencesHelper : ISharedPreferencesHelper {

    override fun onApplicationCreate(context: Context,
                                     sharedPreferencesStorage: ISharedPreferencesStorage,
                                     retrofitHolder: RetrofitHolder,
                                     uiParams: UiParams,
                                     uiUtils: UiUtils,
                                     viewUtils: ViewUtils,
                                     deviceUtils: DeviceUtils) {
        setDefaultImageWidth(context, sharedPreferencesStorage, uiParams, uiUtils, deviceUtils)
        setRetrofitBaseUrl(sharedPreferencesStorage, retrofitHolder)
        setShortInfoHeight(context, sharedPreferencesStorage, uiParams, viewUtils)
        setMaxLines(sharedPreferencesStorage, uiParams)
        setCommentTextSize(sharedPreferencesStorage, uiParams)
        setCommentTextPaint(context, uiParams)
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
                                deviceUtils.getMaximumDisplayWidthInPx(context), context)
                        uiParams.imageWidthDp = newImageWidth
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
                .subscribe(retrofitHolder::changeBaseUrl)
    }

    private fun setShortInfoHeight(context: Context,
                                   sharedPreferencesStorage: ISharedPreferencesStorage,
                                   uiParams: UiParams,
                                   viewUtils: ViewUtils) {
        sharedPreferencesStorage.readInt(
                SharedPreferencesKeystore.THREAD_POST_ITEM_SHORT_INFO_HEIGHT_IN_PX_KEY,
                SharedPreferencesKeystore.THREAD_POST_ITEM_SHORT_INFO_HEIGHT_DEFAULT)
                .subscribeOn(Schedulers.io())
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
                        { uiParams.threadPostItemShortInfoHeight = it },
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