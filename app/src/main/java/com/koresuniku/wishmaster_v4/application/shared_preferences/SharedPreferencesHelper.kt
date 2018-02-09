package com.koresuniku.wishmaster_v4.application.shared_preferences

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import com.koresuniku.wishmaster_v4.R
import com.koresuniku.wishmaster_v4.core.network.client.RetrofitHolder
import com.koresuniku.wishmaster_v4.ui.util.DeviceUtils
import com.koresuniku.wishmaster_v4.ui.util.UiUtils
import com.koresuniku.wishmaster_v4.ui.util.ViewUtils
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.functions.Function7
import io.reactivex.schedulers.Schedulers

/**
 * Created by koresuniku on 14.01.18.
 */

class SharedPreferencesHelper : ISharedPreferencesHelper{

    override fun onApplicationCreate(context: Context,
                                     sharedPreferencesStorage: ISharedPreferencesStorage,
                                     retrofitHolder: RetrofitHolder,
                                     sharedPreferencesUIDimens: SharedPreferencesUiDimens) {
        setDefaultImageWidth(context, sharedPreferencesStorage, sharedPreferencesUIDimens)
        setRetrofitBaseUrl(sharedPreferencesStorage, retrofitHolder)
        setShortInfoHeight(context, sharedPreferencesStorage, sharedPreferencesUIDimens)
    }

    private fun setDefaultImageWidth(context: Context,
                                     sharedPreferencesStorage: ISharedPreferencesStorage,
                                     sharedPreferencesUIDimens: SharedPreferencesUiDimens) {
        readDefaultImageWidthDependentValues(sharedPreferencesStorage)
                .subscribeOn(Schedulers.io())
                .subscribe( { values ->
                    if (values.imageWidthDp == SharedPreferencesKeystore.DEFAULT_IMAGE_WIDTH_IN_DP_DEFAULT) {
                        val newImageWidth = UiUtils.getDefaultImageWidthInDp(
                                DeviceUtils.getMaximumDisplayWidthInPx(context), context)
                        sharedPreferencesUIDimens.imageWidthDp = newImageWidth
                        if (sharedPreferencesStorage.writeIntSameThread(
                                        SharedPreferencesKeystore.DEFAULT_IMAGE_WIDTH_IN_DP_KEY, newImageWidth)) {
                            computeThreadPostItemWidth(context, sharedPreferencesStorage, sharedPreferencesUIDimens)
                                    .subscribeOn(Schedulers.computation())
                                    .subscribe()
                        }
                    } else if (values.threadPostItemHorizontalWidth == SharedPreferencesKeystore.THREAD_POST_ITEM_WIDTH_DEFAULT
                            || values.threadPostItemVerticalWidth == SharedPreferencesKeystore.THREAD_POST_ITEM_WIDTH_DEFAULT
                            || values.threadPostItemSingleImageHorizontalWidth == SharedPreferencesKeystore.THREAD_POST_ITEM_WIDTH_SINGLE_IMAGE_DEFAULT
                            || values.threadPostItemSingleImageVerticalWidth == SharedPreferencesKeystore.THREAD_POST_ITEM_WIDTH_SINGLE_IMAGE_DEFAULT) {
                        computeThreadPostItemWidth(context, sharedPreferencesStorage, sharedPreferencesUIDimens)
                                .subscribeOn(Schedulers.computation())
                                .subscribe()
                    } else {
                        sharedPreferencesUIDimens.imageWidthDp = values.imageWidthDp
                        sharedPreferencesUIDimens.minImageHeightDp = values.minImageHeightDp
                        sharedPreferencesUIDimens.maxImageHeightDp = values.maxImageHeightDp
                        sharedPreferencesUIDimens.threadPostItemHorizontalWidth = values.threadPostItemHorizontalWidth
                        sharedPreferencesUIDimens.threadPostItemVerticalWidth = values.threadPostItemVerticalWidth
                        sharedPreferencesUIDimens.threadPostItemSingleImageHorizontalWidth = values.threadPostItemSingleImageHorizontalWidth
                        sharedPreferencesUIDimens.threadPostItemSingleImageVerticalWidth = values.threadPostItemSingleImageVerticalWidth
                    }
                })

    }

    private fun readDefaultImageWidthDependentValues(
            sharedPreferencesStorage: ISharedPreferencesStorage) :
            Single<SharedPreferencesUiDimens> {
        return Single.zip(
                readDefaultImageWidth(sharedPreferencesStorage),
                readMinImageHeight(sharedPreferencesStorage),
                readMaxImageHeight(sharedPreferencesStorage),
                readThreadPostItemHorizontalWidth(sharedPreferencesStorage),
                readThreadPostItemVerticalWidth(sharedPreferencesStorage),
                readThreadPostItemSingleImageHorizontalWidth(sharedPreferencesStorage),
                readThreadPostItemSingleImageVerticalWidth(sharedPreferencesStorage),
                Function7({ imageWidth: Int, mih: Int, Mih: Int, hw: Int, vw: Int, sihw: Int, sivw: Int -> kotlin.run {
                    val params = SharedPreferencesUiDimens()
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
                                   sharedPreferencesUIDimens: SharedPreferencesUiDimens) {
        sharedPreferencesStorage.readInt(
                SharedPreferencesKeystore.THREAD_POST_ITEM_SHORT_INFO_HEIGHT_IN_PX_KEY,
                SharedPreferencesKeystore.THREAD_POST_ITEM_SHORT_INFO_HEIGHT_DEFAULT)
                .subscribeOn(Schedulers.io())
                .filter { it == SharedPreferencesKeystore.THREAD_POST_ITEM_SHORT_INFO_HEIGHT_DEFAULT }
                .subscribeOn(Schedulers.io())
                .subscribe({
                    val height = computeShortInfoHeight(context)
                    Log.d("SPH", "height $height")
                    sharedPreferencesStorage.writeIntBackground(
                            SharedPreferencesKeystore.THREAD_POST_ITEM_SHORT_INFO_HEIGHT_IN_PX_KEY,
                            height)
                    sharedPreferencesUIDimens.threadPostItemShortInfoHeight = height
                }, { it.printStackTrace() })
    }

    private fun computeShortInfoHeight(context: Context): Int {
        val testImageLayout = LayoutInflater
                .from(context)
                .inflate(R.layout.image_layout, null, false)
        val testShortInfoTextView = testImageLayout.findViewById<TextView>(R.id.summary)
        ViewUtils.measureView(testShortInfoTextView)
        return testShortInfoTextView.measuredHeight
    }

    private fun computeThreadPostItemWidth(context: Context,
                                           sharedPreferencesStorage: ISharedPreferencesStorage,
                                           sharedPreferencesUIDimens: SharedPreferencesUiDimens): Completable {
        return Completable.create({ e -> kotlin.run {
            var displayWidth = DeviceUtils.getDisplayWidth(context)
            var displayHeight = DeviceUtils.getDisplayHeight(context)

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
                    .map({ UiUtils.convertDpToPixel(it.toFloat()).toInt() })
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

                        sharedPreferencesUIDimens.threadPostItemHorizontalWidth = threadPostItemHorizontalWidth
                        sharedPreferencesUIDimens.threadPostItemVerticalWidth = threadPostItemVerticalWidth
                        sharedPreferencesUIDimens.threadPostItemSingleImageHorizontalWidth = threadPostItemSingleImageHorizontal
                        sharedPreferencesUIDimens.threadPostItemSingleImageVerticalWidth = threadPostItemSingleImageVertical

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