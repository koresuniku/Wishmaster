package com.koresuniku.wishmaster_v4.core.thread_list.view

import android.text.Spanned
import com.koresuniku.wishmaster_v4.core.gallery.ImageItemData

/**
 * Created by koresuniku on 07.01.18.
 */

interface ThreadItemView {
    fun setSubject(subject: Spanned, hasImages: Boolean)
    fun setComment(comment: Spanned)
    fun setResumeInfo(resume: String)
    fun setSingleImage(imageItemData: ImageItemData)
    fun setMultipleImages(imageItemDataList: List<ImageItemData>)
}