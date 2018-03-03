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
    fun setMultipleImages(imageItemDataList: List<ImageItemData>,
                          url: String,
                          imageUtils: WishmasterImageUtils,
                          gridViewHeight: Int,
                          summaryHeight: Int)

}