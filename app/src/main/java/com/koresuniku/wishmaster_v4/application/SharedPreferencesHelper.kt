package com.koresuniku.wishmaster_v4.application

import android.content.Context
import com.koresuniku.wishmaster_v4.R
import com.koresuniku.wishmaster_v4.core.network.client.RetrofitHolder
import com.koresuniku.wishmaster_v4.ui.util.DeviceUtils
import com.koresuniku.wishmaster_v4.ui.util.UiUtils
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.functions.Function5
import io.reactivex.schedulers.Schedulers

/**
 * Created by koresuniku on 14.01.18.
 */

object SharedPreferencesHelper {

    fun onApplicationCreate(context: Context,
                            ISharedPreferencesStorage: ISharedPreferencesStorage,
                            retrofitHolder: RetrofitHolder) {
        setDefaultImageWidth(context, ISharedPreferencesStorage)
        setRetrofitBaseUrl(ISharedPreferencesStorage, retrofitHolder)
    }

//    private fun setDefaultImageWidth(context: Context,
//                                     ISharedPreferencesStorage: ISharedPreferencesStorage) {
//        ISharedPreferencesStorage.readInt(
//                SharedPreferencesKeystore.DEFAULT_IMAGE_WIDTH_IN_DP_KEY,
//                SharedPreferencesKeystore.DEFAULT_IMAGE_WIDTH_IN_DP_DEFAULT)
//                .subscribeOn(Schedulers.io())
//                .filter { value -> value == SharedPreferencesKeystore.DEFAULT_IMAGE_WIDTH_IN_DP_DEFAULT }
//                .subscribe {
//                    if (ISharedPreferencesStorage.writeIntSameThread(
//                            SharedPreferencesKeystore.DEFAULT_IMAGE_WIDTH_IN_DP_KEY,
//                            UiUtils.getDefaultImageWidthInDp(
//                                    DeviceUtils.getMaximumDisplayWidthInPx(context), context))) {
//                        computeThreadPostItemWidth(context, ISharedPreferencesStorage)
//                                .subscribeOn(Schedulers.computation())
//                                .subscribe()
//                    }
//                }
//    }

    private fun setDefaultImageWidth(context: Context,
                                     ISharedPreferencesStorage: ISharedPreferencesStorage) {
        readDefaultImageWidthDependentValues(ISharedPreferencesStorage)
                .subscribeOn(Schedulers.io())
                .subscribe( { values ->
                    if (values.imageWidth == SharedPreferencesKeystore.DEFAULT_IMAGE_WIDTH_IN_DP_DEFAULT) {
                        if (ISharedPreferencesStorage.writeIntSameThread(
                                SharedPreferencesKeystore.DEFAULT_IMAGE_WIDTH_IN_DP_KEY,
                                UiUtils.getDefaultImageWidthInDp(
                                        DeviceUtils.getMaximumDisplayWidthInPx(context), context))) {
                            computeThreadPostItemWidth(context, ISharedPreferencesStorage)
                                    .subscribeOn(Schedulers.computation())
                                    .subscribe()
                        }
                    } else {
                        computeThreadPostItemWidth(context, ISharedPreferencesStorage)
                                .subscribeOn(Schedulers.computation())
                                .subscribe()
                    }
                })
    }

    private fun readDefaultImageWidthDependentValues(
            ISharedPreferencesStorage: ISharedPreferencesStorage) :
            Single<DefaultImageWidthDependentValues> {
        return Single.zip(
                readDefaultImageWidth(ISharedPreferencesStorage),
                readThreadPostItemHorizontalWidth(ISharedPreferencesStorage),
                readThreadPostItemVerticalWidth(ISharedPreferencesStorage),
                readThreadPostItemSingleImageHorizontalWidth(ISharedPreferencesStorage),
                readThreadPostItemSingleImageVerticalWidth(ISharedPreferencesStorage),
                Function5({ imageWidth, hw, vw, sihw, sivw ->
                    DefaultImageWidthDependentValues(imageWidth, hw, vw, sihw, sivw)
                }))
    }

    private fun setRetrofitBaseUrl(ISharedPreferencesStorage: ISharedPreferencesStorage,
                                   retrofitHolder: RetrofitHolder) {
        ISharedPreferencesStorage.readString(
                SharedPreferencesKeystore.BASE_URL_KEY,
                SharedPreferencesKeystore.BASE_URL_DEFAULT)
                .subscribeOn(Schedulers.io())
                .filter { value -> value != SharedPreferencesKeystore.BASE_URL_DEFAULT }
                .subscribe(retrofitHolder::changeBaseUrl)
    }

    private fun computeThreadPostItemWidth(context: Context,
                                           ISharedPreferencesStorage: ISharedPreferencesStorage): Completable {
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

            ISharedPreferencesStorage.readInt(
                    SharedPreferencesKeystore.DEFAULT_IMAGE_WIDTH_IN_DP_KEY,
                    SharedPreferencesKeystore.DEFAULT_IMAGE_WIDTH_IN_DP_DEFAULT)
                    .subscribeOn(Schedulers.io())
                    .subscribe({ value -> kotlin.run {
                        val threadPostItemSingleImageHorizontal = threadPostItemHorizontalWidth - value - sideMargin
                        val threadPostItemSingleImageVertical = threadPostItemVerticalWidth - value - sideMargin

                        ISharedPreferencesStorage.writeIntBackground(
                                SharedPreferencesKeystore.THREAD_POST_ITEM_WIDTH_HORIZONTAL_IN_PX_KEY,
                                threadPostItemHorizontalWidth)
                        ISharedPreferencesStorage.writeIntBackground(
                                SharedPreferencesKeystore.THREAD_POST_ITEM_WIDTH_VERTICAL_IN_PX_KEY,
                                threadPostItemVerticalWidth)
                        ISharedPreferencesStorage.writeIntBackground(
                                SharedPreferencesKeystore.THREAD_POST_ITEM_WIDTH_SINGLE_IMAGE_HORIZONTAL_IN_PX_KEY,
                                threadPostItemSingleImageHorizontal)
                        ISharedPreferencesStorage.writeIntBackground(
                                SharedPreferencesKeystore.THREAD_POST_ITEM_WIDTH_SINGLE_IMAGE_VERTICAL_IN_PX_KEY,
                                threadPostItemSingleImageVertical)

                        e.onComplete()
                    }})
        }})
    }

    private fun readDefaultImageWidth(ISharedPreferencesStorage: ISharedPreferencesStorage): Single<Int> {
        return Single.create({ e ->
                ISharedPreferencesStorage.readInt(
                        SharedPreferencesKeystore.DEFAULT_IMAGE_WIDTH_IN_DP_KEY,
                        SharedPreferencesKeystore.DEFAULT_IMAGE_WIDTH_IN_DP_DEFAULT)
                        .observeOn(Schedulers.io())
                        .subscribe(e::onSuccess)
        })
    }

    private fun readThreadPostItemHorizontalWidth(ISharedPreferencesStorage: ISharedPreferencesStorage): Single<Int> {
        return Single.create({ e ->
            ISharedPreferencesStorage.readInt(
                    SharedPreferencesKeystore.THREAD_POST_ITEM_WIDTH_HORIZONTAL_IN_PX_KEY,
                    SharedPreferencesKeystore.THREAD_POST_ITEM_WIDTH_DEFAULT)
                    .observeOn(Schedulers.io())
                    .subscribe(e::onSuccess)
        })
    }

    private fun readThreadPostItemVerticalWidth(ISharedPreferencesStorage: ISharedPreferencesStorage): Single<Int> {
        return Single.create({ e ->
            ISharedPreferencesStorage.readInt(
                    SharedPreferencesKeystore.THREAD_POST_ITEM_WIDTH_VERTICAL_IN_PX_KEY,
                    SharedPreferencesKeystore.THREAD_POST_ITEM_WIDTH_DEFAULT)
                    .observeOn(Schedulers.io())
                    .subscribe(e::onSuccess)
        })
    }

    private fun readThreadPostItemSingleImageHorizontalWidth(ISharedPreferencesStorage: ISharedPreferencesStorage): Single<Int> {
        return Single.create({ e ->
            ISharedPreferencesStorage.readInt(
                    SharedPreferencesKeystore.THREAD_POST_ITEM_WIDTH_SINGLE_IMAGE_HORIZONTAL_IN_PX_KEY,
                    SharedPreferencesKeystore.THREAD_POST_ITEM_WIDTH_SINGLE_IMAGE_DEFAULT)
                    .observeOn(Schedulers.io())
                    .subscribe(e::onSuccess)
        })
    }

    private fun readThreadPostItemSingleImageVerticalWidth(ISharedPreferencesStorage: ISharedPreferencesStorage): Single<Int> {
        return Single.create({ e ->
            ISharedPreferencesStorage.readInt(
                    SharedPreferencesKeystore.THREAD_POST_ITEM_WIDTH_SINGLE_IMAGE_VERTICAL_IN_PX_KEY,
                    SharedPreferencesKeystore.THREAD_POST_ITEM_WIDTH_SINGLE_IMAGE_DEFAULT)
                    .observeOn(Schedulers.io())
                    .subscribe(e::onSuccess)
        })
    }

    private data class DefaultImageWidthDependentValues(val imageWidth: Int,
                                                        val threadPostItemHorizontalWidth: Int,
                                                        val threadPostItemVerticalWidth: Int,
                                                        val threadPostItemSingleImageHorizontalWidth: Int,
                                                        val threadPostItemSingleImageVerticalWidth: Int)
}