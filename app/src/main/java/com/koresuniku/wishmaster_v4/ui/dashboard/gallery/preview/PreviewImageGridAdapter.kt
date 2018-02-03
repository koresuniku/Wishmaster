package com.koresuniku.wishmaster_v4.ui.dashboard.gallery.preview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.koresuniku.wishmaster_v4.R
import com.koresuniku.wishmaster_v4.core.gallery.ImageItemData
import com.koresuniku.wishmaster_v4.core.gallery.WishmasterImageUtils

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
                //WishmasterImageUtils.loadImageThumbnail(imageItemData, image, baseUrl)

            }
        }
        return returnView ?: View(parent?.context)
    }

    override fun getItem(position: Int): Any = imageItemDataList[position].file
    override fun getItemId(position: Int): Long = position.toLong()
    override fun getCount(): Int = imageItemDataList.count()
}