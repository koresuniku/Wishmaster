package com.koresuniku.wishmaster_v4.application.shared_preferences

import android.content.Context
import android.util.Log
import com.koresuniku.wishmaster_v4.R
import com.koresuniku.wishmaster_v4.core.network.client.RetrofitHolder
import com.koresuniku.wishmaster_v4.ui.util.DeviceUtils
import com.koresuniku.wishmaster_v4.ui.util.UiUtils
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
                            sharedPreferencesUIParams: ISharedPreferencesUiParams) {
        setDefaultImageWidth(context, sharedPreferencesStorage, sharedPreferencesUIParams)
        setRetrofitBaseUrl(sharedPreferencesStorage, retrofitHolder)
    }

    private fun setDefaultImageWidth(context: Context,
                                     sharedPreferencesStorage: ISharedPreferencesStorage,
                                     sharedPreferencesUIParams: ISharedPreferencesUiParams) {
        readDefaultImageWidthDependentValues(sharedPreferencesStorage)
                .subscribeOn(Schedulers.io())
                .subscribe( { values ->
                    if (values.imageWidth == SharedPreferencesKeystore.DEFAULT_IMAGE_WIDTH_IN_DP_DEFAULT) {
                        val newImageWidth = UiUtils.getDefaultImageWidthInDp(
                                DeviceUtils.getMaximumDisplayWidthInPx(context), context)
                        values.imageWidth = newImageWidth
                        if (sharedPreferencesStorage.writeIntSameThread(
                                        SharedPreferencesKeystore.DEFAULT_IMAGE_WIDTH_IN_DP_KEY, newImageWidth)) {
                            computeThreadPostItemWidth(context, sharedPreferencesStorage, sharedPreferencesUIParams)
                                    .subscribeOn(Schedulers.computation())
                                    .subscribe()
                        }
                    } else if (values.threadPostItemHorizontalWidth == SharedPreferencesKeystore.THREAD_POST_ITEM_WIDTH_DEFAULT
                            || values.threadPostItemVerticalWidth == SharedPreferencesKeystore.THREAD_POST_ITEM_WIDTH_DEFAULT
                            || values.threadPostItemSingleImageHorizontalWidth == SharedPreferencesKeystore.THREAD_POST_ITEM_WIDTH_SINGLE_IMAGE_DEFAULT
                            || values.threadPostItemSingleImageVerticalWidth == SharedPreferencesKeystore.THREAD_POST_ITEM_WIDTH_SINGLE_IMAGE_DEFAULT) {
                        computeThreadPostItemWidth(context, sharedPreferencesStorage, sharedPreferencesUIParams)
                                .subscribeOn(Schedulers.computation())
                                .subscribe()
                    } else {
                        Log.d("SPI", values.imageWidth.toString())
                        sharedPreferencesUIParams.imageWidth = values.imageWidth
                        sharedPreferencesUIParams.minImageHeight = values.minImageHeight
                        sharedPreferencesUIParams.maxImageHeight = values.maxImageHeight
                        sharedPreferencesUIParams.threadPostItemHorizontalWidth = values.threadPostItemHorizontalWidth
                        sharedPreferencesUIParams.threadPostItemVerticalWidth = values.threadPostItemVerticalWidth
                        sharedPreferencesUIParams.threadPostItemSingleImageHorizontalWidth = values.threadPostItemSingleImageHorizontalWidth
                        sharedPreferencesUIParams.threadPostItemSingleImageVerticalWidth = values.threadPostItemSingleImageVerticalWidth
                    }
                })
    }

    private fun readDefaultImageWidthDependentValues(
            sharedPreferencesStorage: ISharedPreferencesStorage) :
            Single<SharedPreferencesUiParams> {
        return Single.zip(
                readDefaultImageWidth(sharedPreferencesStorage),
                readMinImageHeight(sharedPreferencesStorage),
                readMaxImageHeight(sharedPreferencesStorage),
                readThreadPostItemHorizontalWidth(sharedPreferencesStorage),
                readThreadPostItemVerticalWidth(sharedPreferencesStorage),
                readThreadPostItemSingleImageHorizontalWidth(sharedPreferencesStorage),
                readThreadPostItemSingleImageVerticalWidth(sharedPreferencesStorage),
                Function7({ imageWidth: Int, mih: Int, Mih: Int, hw: Int, vw: Int, sihw: Int, sivw: Int -> kotlin.run {
                    val params = SharedPreferencesUiParams()
                    params.imageWidth = imageWidth
                    params.minImageHeight = mih
                    params.maxImageHeight = Mih
                    params.threadPostItemHorizontalWidth = hw
                    params.threadPostItemVerticalWidth = vw
                    params.threadPostItemSingleImageHorizontalWidth = sihw
                    params.threadPostItemSingleImageVerticalWidth = sivw
                     params
                }
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

    private fun computeThreadPostItemWidth(context: Context,
                                           sharedPreferencesStorage: ISharedPreferencesStorage,
                                           sharedPreferencesUIParams: ISharedPreferencesUiParams): Completable {
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
                    .subscribe({ value -> kotlin.run {
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

                        sharedPreferencesUIParams.threadPostItemHorizontalWidth = threadPostItemHorizontalWidth
                        sharedPreferencesUIParams.threadPostItemVerticalWidth = threadPostItemVerticalWidth
                        sharedPreferencesUIParams.threadPostItemSingleImageHorizontalWidth = threadPostItemSingleImageHorizontal
                        sharedPreferencesUIParams.threadPostItemSingleImageVerticalWidth = threadPostItemSingleImageVertical

                        e.onComplete()
                    }})
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