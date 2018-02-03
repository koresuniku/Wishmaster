package com.koresuniku.wishmaster_v4.core.gallery

import android.net.Uri
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.koresuniku.wishmaster_v4.R
import com.koresuniku.wishmaster_v4.application.shared_preferences.SharedPreferencesKeystore
import com.koresuniku.wishmaster_v4.application.shared_preferences.ISharedPreferencesStorage
import com.koresuniku.wishmaster_v4.application.shared_preferences.SharedPreferencesUiDimens
import com.koresuniku.wishmaster_v4.core.data.model.threads.File
import com.koresuniku.wishmaster_v4.core.util.text.WishmasterTextUtils
import com.koresuniku.wishmaster_v4.ui.util.UiUtils
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function3
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by koresuniku on 14.01.18.
 */

class WishmasterImageUtils @Inject constructor(private val textUtils: WishmasterTextUtils,
        private val sharedPreferencesUiDimens: SharedPreferencesUiDimens) {

    fun getImageItemData(file: File): Single<ImageItemData> {
        return Single.create({
            it.onSuccess(ImageItemData(
                    file,
                    computeImageLayoutConfiguration(file, sharedPreferencesUiDimens),
                    textUtils.obtainImageResume(file)))
        })
    }

    fun getImageItemData(files: List<File>): Single<List<ImageItemData>> {
        return Single.create({
            val configs: MutableList<ImageLayoutDimensions> = ArrayList()
            val summaries: MutableList<String> = ArrayList()
            val imageItemDataList: MutableList<ImageItemData> = ArrayList()

            files.mapTo(configs) { computeImageLayoutConfiguration(it, sharedPreferencesUiDimens) }
            files.mapTo(summaries) { textUtils.obtainImageResume(it) }

            files.mapIndexed{ index, file ->
                imageItemDataList.add(ImageItemData(file, configs[index], summaries[index]))}
            it.onSuccess(imageItemDataList)
        })
    }

    private fun computeImageLayoutConfiguration(file: File,
                                                sharedPreferencesUiDimens: SharedPreferencesUiDimens):
            ImageLayoutDimensions {
        val fileWidth = file.width.toInt()
        val fileHeight = file.height.toInt()
        val aspectRatio: Float = fileWidth.toFloat() / fileHeight.toFloat()

        val actualWidth = sharedPreferencesUiDimens.imageWidth
        var actualHeight = Math.ceil((actualWidth/ aspectRatio).toDouble()).toInt()
        val min = sharedPreferencesUiDimens.minImageHeight
        val max = sharedPreferencesUiDimens.maxImageHeight

        if (min > actualHeight) actualHeight = min
        if (max < actualHeight) actualHeight = max

        return ImageLayoutDimensions(actualWidth, actualHeight)
    }

//    private fun getImageConfigurationFromSharedPreferences(ISharedPreferencesStorage: ISharedPreferencesStorage,
//                                                           compositeDisposable: CompositeDisposable) : Single<ImageSharedPreferencesConfiguration> {
//        return Single.zip(
//                getDefaultImageWidthInDpSingle(ISharedPreferencesStorage, compositeDisposable).observeOn(Schedulers.computation()),
//                getMinimumImageHeightInDpSingle(ISharedPreferencesStorage, compositeDisposable).observeOn(Schedulers.computation()),
//                getMaximumImageHeightInDpSingle(ISharedPreferencesStorage, compositeDisposable).observeOn(Schedulers.computation()),
//                Function3({ width, min, max -> ImageSharedPreferencesConfiguration(
//                        UiUtils.convertDpToPixel(width.toFloat()).toInt(),
//                        UiUtils.convertDpToPixel(min.toFloat()).toInt(),
//                        UiUtils.convertDpToPixel(max.toFloat()).toInt())
//                }))
//    }
//
//    private fun getDefaultImageWidthInDpSingle(ISharedPreferencesStorage: ISharedPreferencesStorage,
//                                               compositeDisposable: CompositeDisposable): Single<Int> {
//        return Single.create({ e ->
//            compositeDisposable.add(ISharedPreferencesStorage.readInt(
//                    SharedPreferencesKeystore.DEFAULT_IMAGE_WIDTH_IN_DP_KEY,
//                    SharedPreferencesKeystore.DEFAULT_IMAGE_WIDTH_IN_DP_DEFAULT)
//                    .observeOn(Schedulers.io()).subscribe(e::onSuccess))
//        })
//    }
//
//    private fun getMinimumImageHeightInDpSingle(ISharedPreferencesStorage: ISharedPreferencesStorage,
//                                                compositeDisposable: CompositeDisposable): Single<Int> {
//        return Single.create({ e ->
//            compositeDisposable.add(ISharedPreferencesStorage.readInt(
//                    SharedPreferencesKeystore.MINIMUM_IMAGE_HEIGHT_IN_DP_KEY,
//                    SharedPreferencesKeystore.MINIMUM_IMAGE_HEIGHT_IN_DP_DEFAULT)
//                    .observeOn(Schedulers.io()).subscribe(e::onSuccess))
//        })
//    }
//
//    private fun getMaximumImageHeightInDpSingle(ISharedPreferencesStorage: ISharedPreferencesStorage,
//                                                compositeDisposable: CompositeDisposable): Single<Int> {
//        return Single.create({ e ->
//            compositeDisposable.add(ISharedPreferencesStorage.readInt(
//                    SharedPreferencesKeystore.MAXIMUM_IMAGE_HEIGHT_IN_DP_KEY,
//                    SharedPreferencesKeystore.MAXIMUM_IMAGE_HEIGHT_IN_DP_DEFAULT)
//                    .observeOn(Schedulers.io()).subscribe(e::onSuccess))
//        })
//    }
//
//    private data class ImageSharedPreferencesConfiguration(val width: Int, val min: Int, val max: Int)

    fun loadImageThumbnail(imageItemData: ImageItemData, image: ImageView, baseUrl: String) {

        Log.d("WIU", "imageItgemData: ${imageItemData.dimensions.widthInPx}")
        image.layoutParams.width = imageItemData.dimensions.widthInPx
        image.layoutParams.height = imageItemData.dimensions.heightInPx
        image.setImageBitmap(null)
        image.animation?.cancel()
        image.setBackgroundColor(image.context.resources.getColor(R.color.colorBackgroundDark))


        Glide.with(image.context)
                .load(Uri.parse(baseUrl + imageItemData.file.thumbnail))
                .crossFade(200)
                .placeholder(image.drawable)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(image)
    }
}