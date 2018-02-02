package com.koresuniku.wishmaster_v4.core.gallery

import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.koresuniku.wishmaster_v4.R
import com.koresuniku.wishmaster_v4.application.shared_preferences.SharedPreferencesKeystore
import com.koresuniku.wishmaster_v4.application.shared_preferences.ISharedPreferencesStorage
import com.koresuniku.wishmaster_v4.core.data.model.threads.File
import com.koresuniku.wishmaster_v4.core.util.text.WishmasterTextUtils
import com.koresuniku.wishmaster_v4.ui.util.UiUtils
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function3
import io.reactivex.schedulers.Schedulers

/**
 * Created by koresuniku on 14.01.18.
 */

object WishmasterImageUtils {

    fun getImageItemData(file: File,
                         ISharedPreferencesStorage: ISharedPreferencesStorage,
                         compositeDisposable: CompositeDisposable): Single<ImageItemData> {
        return Single.create({ e ->
            compositeDisposable.add(getImageConfigurationFromSharedPreferences(
                    ISharedPreferencesStorage, compositeDisposable)
                    .subscribe({ config -> e.onSuccess(ImageItemData(
                            file,
                            computeImageLayoutConfiguration(file, config),
                            WishmasterTextUtils.obtainImageResume(file)))
                    }))
        })
    }

    fun getImageItemData(files: List<File>,
                         ISharedPreferencesStorage: ISharedPreferencesStorage,
                         compositeDisposable: CompositeDisposable): Single<List<ImageItemData>> {
        return Single.create({ e ->
            compositeDisposable.add(getImageConfigurationFromSharedPreferences(
                    ISharedPreferencesStorage, compositeDisposable)
                    .subscribe({ config -> run {
                        val configs: MutableList<ImageLayoutConfiguration> = ArrayList()
                        val summaries: MutableList<String> = ArrayList()
                        val imageItemDataList: MutableList<ImageItemData> = ArrayList()
                        files.mapTo(configs) { computeImageLayoutConfiguration(it, config) }
                        files.mapTo(summaries) { WishmasterTextUtils.obtainImageResume(it) }

                        files.mapIndexed{ index, file ->
                            imageItemDataList.add(ImageItemData(file, configs[index], summaries[index]))}
                        e.onSuccess(imageItemDataList)
                    } }))
        })
    }

    private fun computeImageLayoutConfiguration(file: File,
                                                imageSharedPreferencesConfiguration: ImageSharedPreferencesConfiguration) : ImageLayoutConfiguration {
        val fileWidth = file.width.toInt()
        val fileHeight = file.height.toInt()
        val aspectRatio: Float = fileWidth.toFloat() / fileHeight.toFloat()

        val actualWidth = imageSharedPreferencesConfiguration.width
        var actualHeight = Math.ceil((actualWidth/ aspectRatio).toDouble()).toInt()
        val min = imageSharedPreferencesConfiguration.min
        val max = imageSharedPreferencesConfiguration.max

        if (min > actualHeight) actualHeight = min
        if (max < actualHeight) actualHeight = max

        return ImageLayoutConfiguration(actualWidth, actualHeight)
    }

    private fun getImageConfigurationFromSharedPreferences(ISharedPreferencesStorage: ISharedPreferencesStorage,
                                                           compositeDisposable: CompositeDisposable) : Single<ImageSharedPreferencesConfiguration> {
        return Single.zip(
                getDefaultImageWidthInDpSingle(ISharedPreferencesStorage, compositeDisposable).observeOn(Schedulers.computation()),
                getMinimumImageHeightInDpSingle(ISharedPreferencesStorage, compositeDisposable).observeOn(Schedulers.computation()),
                getMaximumImageHeightInDpSingle(ISharedPreferencesStorage, compositeDisposable).observeOn(Schedulers.computation()),
                Function3({ width, min, max -> ImageSharedPreferencesConfiguration(
                        UiUtils.convertDpToPixel(width.toFloat()).toInt(),
                        UiUtils.convertDpToPixel(min.toFloat()).toInt(),
                        UiUtils.convertDpToPixel(max.toFloat()).toInt())
                }))
    }

    private fun getDefaultImageWidthInDpSingle(ISharedPreferencesStorage: ISharedPreferencesStorage,
                                               compositeDisposable: CompositeDisposable): Single<Int> {
        return Single.create({ e ->
            compositeDisposable.add(ISharedPreferencesStorage.readInt(
                    SharedPreferencesKeystore.DEFAULT_IMAGE_WIDTH_IN_DP_KEY,
                    SharedPreferencesKeystore.DEFAULT_IMAGE_WIDTH_IN_DP_DEFAULT)
                    .observeOn(Schedulers.io()).subscribe(e::onSuccess))
        })
    }

    private fun getMinimumImageHeightInDpSingle(ISharedPreferencesStorage: ISharedPreferencesStorage,
                                                compositeDisposable: CompositeDisposable): Single<Int> {
        return Single.create({ e ->
            compositeDisposable.add(ISharedPreferencesStorage.readInt(
                    SharedPreferencesKeystore.MINIMUM_IMAGE_HEIGHT_IN_DP_KEY,
                    SharedPreferencesKeystore.MINIMUM_IMAGE_HEIGHT_IN_DP_DEFAULT)
                    .observeOn(Schedulers.io()).subscribe(e::onSuccess))
        })
    }

    private fun getMaximumImageHeightInDpSingle(ISharedPreferencesStorage: ISharedPreferencesStorage,
                                                compositeDisposable: CompositeDisposable): Single<Int> {
        return Single.create({ e ->
            compositeDisposable.add(ISharedPreferencesStorage.readInt(
                    SharedPreferencesKeystore.MAXIMUM_IMAGE_HEIGHT_IN_DP_KEY,
                    SharedPreferencesKeystore.MAXIMUM_IMAGE_HEIGHT_IN_DP_DEFAULT)
                    .observeOn(Schedulers.io()).subscribe(e::onSuccess))
        })
    }

    private data class ImageSharedPreferencesConfiguration(val width: Int, val min: Int, val max: Int)

    fun loadImageThumbnail(imageItemData: ImageItemData, image: ImageView, baseUrl: String) {
        image.layoutParams.width = imageItemData.configuration.widthInPx
        image.layoutParams.height = imageItemData.configuration.heightInPx
        //image.requestLayout()
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