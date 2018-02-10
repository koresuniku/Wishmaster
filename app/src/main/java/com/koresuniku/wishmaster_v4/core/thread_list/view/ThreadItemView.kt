package com.koresuniku.wishmaster_v4.core.thread_list.view

import android.text.Spanned
import com.koresuniku.wishmaster_v4.core.gallery.ImageItemData
import com.koresuniku.wishmaster_v4.core.gallery.WishmasterImageUtils

/**
 * Created by koresuniku on 07.01.18.
 */

interface ThreadItemView {
    fun switchSubjectVisibility(visible: Boolean)
    fun setSubject(subject: Spanned)
    fun setComment(comment: Spanned)
    fun setThreadShortInfo(info: String)
    fun setSingleImage(imageItemData: ImageItemData, url: String, imageUtils: WishmasterImageUtils)
    fun setMultipleImages(imageItemDataList: List<ImageItemData>, url: String, imageUtils: WishmasterImageUtils, gridViewHeight: Int)
}