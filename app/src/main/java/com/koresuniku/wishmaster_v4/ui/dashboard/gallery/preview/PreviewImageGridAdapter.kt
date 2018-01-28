package com.koresuniku.wishmaster_v4.ui.dashboard.gallery.preview

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.koresuniku.wishmaster_v4.R
import com.koresuniku.wishmaster_v4.core.data.threads.File
import com.koresuniku.wishmaster_v4.core.gallery.ImageItemData
import com.koresuniku.wishmaster_v4.core.gallery.ImageLayoutConfiguration
import com.koresuniku.wishmaster_v4.core.gallery.WishmasterImageUtils
import com.koresuniku.wishmaster_v4.core.util.text.WishmasterTextUtils
import com.koresuniku.wishmaster_v4.ui.util.UiUtils
import com.koresuniku.wishmaster_v4.ui.util.ViewUtils
import com.koresuniku.wishmaster_v4.ui.view.LayoutWrapContentUpdater
import com.koresuniku.wishmaster_v4.ui.view.widget.ExpandableHeightGridView

/**
 * Created by koresuniku on 15.01.18.
 */

class PreviewImageGridAdapter(private val imageItemDataList: List<ImageItemData>,
                              private val baseUrl: String) : BaseAdapter() {



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

                imageSummary.text = imageItemData.summary
                WishmasterImageUtils.loadImageThumbnail(imageItemData, image, baseUrl)

            }
        }
        return returnView ?: View(parent?.context)
    }

//    private fun adjustHeight(gridView: ExpandableHeightGridView, position: Int, itemHeight: Int) {
//        if (position == 0 || (position >= gridView.numColumns && gridView.numColumns % position == 0)) {
//            if (position != 0) lastRowHeight = finalHeight
//            finalHeight += itemHeight
//        } else if (lastRowHeight + itemHeight > finalHeight) {
//            finalHeight = lastRowHeight + itemHeight
//        }
//        if (lastItemCounted) run {
//            Log.d("EHGV", "finalHeight: $finalHeight")
//            finalHeight = 0
//            lastRowHeight = 0
//            lastItemCounted = !lastItemCounted
//            //gridView.layoutParams.height = finalHeight
//            //measure(layoutParams.width, layoutParams.height)
//        }
//    }

    override fun getItem(position: Int): Any = imageItemDataList[position].file
    override fun getItemId(position: Int): Long = position.toLong()
    override fun getCount(): Int = imageItemDataList.count()
}