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

package com.koresuniku.wishmaster.ui.gallery.preview

import android.graphics.Rect
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.core.modules.gallery.ImageItemData
import com.koresuniku.wishmaster.core.utils.images.WishmasterImageUtils
import com.koresuniku.wishmaster.ui.view.widget.WMGridView

/**
 * Created by koresuniku on 15.01.18.
 */

class PreviewImageGridAdapter(private val imageItemDataList: List<ImageItemData>,
                              private val url: String,
                              private val imageUtils: WishmasterImageUtils,
                              private val summaryHeight: Int,
                              private val gridViewParams: WMGridView.GridViewParams,
                              private val onNoItemClickListener: WMGridView.OnNoItemClickListener,
                              private val onImageItemClickListener: WMGridView.OnImageItemClickListener) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var returnView: View? = convertView
        parent?.let {
            if (returnView == null) {
                returnView = LayoutInflater
                        .from(it.context)
                        .inflate(R.layout.image_layout, parent, false)
            }
            returnView?.let {
                val imageItemData = imageItemDataList[position]
                val image = it.findViewById<ImageView>(R.id.image)
                val imageSummary = it.findViewById<TextView>(R.id.summary)
                val clickableItemLayout = it.findViewById<View>(R.id.clickable_item_layout)
                val clickableView = it.findViewById<View>(R.id.clickable_view)

                clickableItemLayout.setOnClickListener {
//                        val rect = Rect()
//                        image.getGlobalVisibleRect(rect)
//                        val coordinates = WishmasterImageUtils.ImageCoordinates(
//                                rect.left, rect.right, rect.top, rect.bottom)
//                        presenter.setPreviewImageCoordinates(coordinates)
//
                    onImageItemClickListener.onImageItemClick(position)
                }
                clickableView.setOnClickListener { onNoItemClickListener.onNoItemClick() }

                imageSummary.text = imageItemData.summary
                imageUtils.loadImageThumbnail(imageItemData, image, url)

                if ((position + 1) % gridViewParams.numColumns == 0) {
                    clickableView.layoutParams.height =
                            gridViewParams.maxHeightInARow[position / gridViewParams.numColumns] -
                            (imageItemData.dimensions.heightInPx + summaryHeight)
                } else clickableView.layoutParams.height = 0
            }
        }
        return returnView ?: View(parent?.context)
    }

    override fun getItem(position: Int): Any = imageItemDataList[position].file
    override fun getItemId(position: Int): Long = position.toLong()
    override fun getCount(): Int = imageItemDataList.count()
}