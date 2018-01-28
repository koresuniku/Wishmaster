package com.koresuniku.wishmaster_v4.core.thread_list

import android.text.Spannable
import android.text.Spanned
import com.koresuniku.wishmaster_v4.core.base.IMvpView
import com.koresuniku.wishmaster_v4.core.data.threads.File
import com.koresuniku.wishmaster_v4.core.gallery.ImageItemData
import com.koresuniku.wishmaster_v4.core.gallery.ImageLayoutConfiguration

/**
 * Created by koresuniku on 07.01.18.
 */

interface ThreadItemView : IMvpView {
    fun setSubject(subject: Spanned, hasImages: Boolean)
    fun setComment(comment: Spanned)
    fun setResumeInfo(resume: String)
    fun setSingleImage(imageItemData: ImageItemData)
    fun setMultipleImages(imageItemDataList: List<ImageItemData>)
}