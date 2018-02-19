package com.koresuniku.wishmaster.core.modules.full_thread

import android.text.Spannable
import android.text.Spanned
import com.koresuniku.wishmaster.core.modules.gallery.ImageItemData
import com.koresuniku.wishmaster.core.utils.images.WishmasterImageUtils

/**
 * Created by koresuniku on 2/14/18.
 */

interface PostItemView {fun setOnClickItemListener(threadNumber: String)
    fun setHeader(head: Spannable)
    fun switchAnswersVisibility(visible: Boolean)
    fun setAnswers(subject: Spanned)
    fun setComment(comment: Spanned)
    fun setSingleImage(imageItemData: ImageItemData, url: String, imageUtils: WishmasterImageUtils)
    fun setMultipleImages(imageItemDataList: List<ImageItemData>, url: String,
                          imageUtils: WishmasterImageUtils, gridViewHeight: Int)

}