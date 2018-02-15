package com.koresuniku.wishmaster.core.utils.images

import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.application.preferences.UiParams
import com.koresuniku.wishmaster.core.data.model.threads.File
import com.koresuniku.wishmaster.core.modules.gallery.ImageItemData
import com.koresuniku.wishmaster.core.modules.gallery.ImageLayoutDimensions
import com.koresuniku.wishmaster.core.utils.text.WishmasterTextUtils
import com.koresuniku.wishmaster.ui.utils.UiUtils
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by koresuniku on 14.01.18.
 */

class WishmasterImageUtils @Inject constructor(private val uiUtils: UiUtils, private val textUtils: WishmasterTextUtils,
        private val uiParams: UiParams) {

    fun getImageItemData(file: File): Single<ImageItemData> {
        //Log.d("WIU", "SPUID: ${uiParams}")
        return Single.create({
            it.onSuccess(ImageItemData(
                    file,
                    computeImageLayoutConfiguration(file, uiParams),
                    textUtils.obtainImageShortInfo(file)))
        })
    }

    fun getImageItemData(files: List<File>): Single<List<ImageItemData>> {
        return Single.create({
            val configs: MutableList<ImageLayoutDimensions> = ArrayList()
            val summaries: MutableList<String> = ArrayList()
            val imageItemDataList: MutableList<ImageItemData> = ArrayList()

            files.mapTo(configs) { computeImageLayoutConfiguration(it, uiParams) }
            files.mapTo(summaries) { textUtils.obtainImageShortInfo(it) }

            files.mapIndexed{ index, file ->
                imageItemDataList.add(ImageItemData(file, configs[index], summaries[index]))}
            it.onSuccess(imageItemDataList)
        })
    }

    private fun computeImageLayoutConfiguration(file: File,
                                                uiParams: UiParams):
            ImageLayoutDimensions {
        val fileWidth = file.width.toInt()
        val fileHeight = file.height.toInt()
        val aspectRatio: Float = fileWidth.toFloat() / fileHeight.toFloat()

        val actualWidth = uiUtils.convertDpToPixel(uiParams.imageWidthDp.toFloat()).toInt()
        var actualHeight = Math.ceil((actualWidth/ aspectRatio).toDouble()).toInt()
        val min = uiUtils.convertDpToPixel(uiParams.minImageHeightDp.toFloat()).toInt()
        val max = uiUtils.convertDpToPixel(uiParams.maxImageHeightDp.toFloat()).toInt()

        if (min > actualHeight) actualHeight = min
        if (max < actualHeight) actualHeight = max

        return ImageLayoutDimensions(actualWidth, actualHeight)
    }

    fun loadImageThumbnail(imageItemData: ImageItemData, image: ImageView, baseUrl: String) {
        //Log.d("WIU", "imageItgemData: ${imageItemData.dimensions.widthInPx}")
        image.layoutParams.width = imageItemData.dimensions.widthInPx
        image.layoutParams.height = imageItemData.dimensions.heightInPx
        image.setImageBitmap(null)
        image.animation?.cancel()
        image.setBackgroundColor(image.context.resources.getColor(R.color.colorBackgroundDark))

        Glide.with(image.context)
                .load(Uri.parse(baseUrl + imageItemData.file.thumbnail))
                .placeholder(image.drawable)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(image)
    }
}