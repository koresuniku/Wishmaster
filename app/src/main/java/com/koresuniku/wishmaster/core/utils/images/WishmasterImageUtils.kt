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

package com.koresuniku.wishmaster.core.utils.images

import android.content.Context
import android.graphics.Point
import android.graphics.PointF
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.application.singletones.UiParams
import com.koresuniku.wishmaster.core.data.model.threads.File
import com.koresuniku.wishmaster.core.modules.gallery.ImageItemData
import com.koresuniku.wishmaster.core.modules.gallery.ImageLayoutDimensions
import com.koresuniku.wishmaster.core.utils.text.WishmasterTextUtils
import com.koresuniku.wishmaster.ui.utils.DeviceUtils
import com.koresuniku.wishmaster.ui.utils.UiUtils
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by koresuniku on 14.01.18.
 */

class WishmasterImageUtils @Inject constructor(private val uiUtils: UiUtils,
                                               private val textUtils: WishmasterTextUtils,
                                               private val uiParams: UiParams,
                                               private val deviceUtils: DeviceUtils) {

    data class ImageTargetDimensions(val width: Int, val height: Int)

    data class ImageCoordinates(val xLeft: Int, val xRight: Int, val yTop: Int, val yBottom: Int)

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

    fun computeImageCoordinates(context: Context, file: File): Single<ImageCoordinates> {
        return Single.create {
            val targetWidth: Int
            val targetHeight: Int

            val displayWidth = deviceUtils.getDisplayWidth(context)
            val displayHeight = deviceUtils.getDisplayHeight(context)

            val rawImageWidth = file.width
            val rawImageHeight = file.height

            val widthByHeightDisplayRatio = displayWidth.toFloat() / displayHeight.toFloat()
            val widthByHeightImageRatio = rawImageWidth.toFloat() / rawImageHeight.toFloat()

            val isDisplayHorizontal = displayWidth > displayHeight
            val doesImageFitHeight = widthByHeightDisplayRatio > widthByHeightImageRatio

            if (doesImageFitHeight) {
                targetHeight = displayHeight
                targetWidth = (targetHeight * widthByHeightImageRatio).toInt()
            } else {
                targetWidth = displayWidth
                targetHeight = (targetWidth / widthByHeightImageRatio).toInt()
            }

            val center = Point(displayWidth / 2, displayHeight / 2)
            val halfTargetWidth = targetWidth / 2
            val halfTargetHeight = targetHeight / 2
            val xLeft = center.x - halfTargetWidth
            val xRight = center.x + halfTargetWidth
            val yTop = center.y - halfTargetHeight
            val yBottom = center.y + halfTargetHeight

            it.onSuccess(ImageCoordinates(xLeft, xRight, yTop, yBottom))
        }
    }
}