package com.koresuniku.wishmaster.core.modules.thread_list

import android.text.Spanned
import com.koresuniku.wishmaster.core.modules.gallery.ImageItemData
import com.koresuniku.wishmaster.core.utils.images.WishmasterImageUtils

/**
 * Created by koresuniku on 07.01.18.
 */

interface ThreadItemView {
    var threadNumber: String
    fun adaptLayout(position: Int)
    fun setOnClickItemListener()
    fun switchSubjectVisibility(visible: Boolean)
    fun setSubject(subject: Spanned)
    fun setMaxLines(value: Int)
    fun setComment(comment: Spanned)
    fun setThreadShortInfo(info: String)
    fun setSingleImage(imageItemData: ImageItemData, url: String, imageUtils: WishmasterImageUtils)
    fun setMultipleImages(imageItemDataList: List<ImageItemData>, url: String, imageUtils: WishmasterImageUtils, gridViewHeight: Int)
}