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

package com.koresuniku.wishmaster.application.global

import android.text.TextPaint
import com.koresuniku.wishmaster.application.preferences.SharedPreferencesKeystore
import javax.inject.Inject


class UiParams @Inject constructor() {
    var imageWidthDp: Int = SharedPreferencesKeystore.DEFAULT_IMAGE_WIDTH_IN_DP_DEFAULT
    var minImageHeightDp: Int = SharedPreferencesKeystore.MINIMUM_IMAGE_HEIGHT_IN_DP_DEFAULT
    var maxImageHeightDp: Int = SharedPreferencesKeystore.MAXIMUM_IMAGE_HEIGHT_IN_DP_DEFAULT
    var threadPostItemHorizontalWidth: Int = SharedPreferencesKeystore.THREAD_POST_ITEM_WIDTH_DEFAULT
    var threadPostItemVerticalWidth: Int = SharedPreferencesKeystore.THREAD_POST_ITEM_WIDTH_DEFAULT
    var threadPostItemSingleImageHorizontalWidth: Int = SharedPreferencesKeystore.THREAD_POST_ITEM_WIDTH_SINGLE_IMAGE_DEFAULT
    var threadPostItemSingleImageVerticalWidth: Int = SharedPreferencesKeystore.THREAD_POST_ITEM_WIDTH_SINGLE_IMAGE_DEFAULT
    var threadPostItemShortInfoHeight: Int = SharedPreferencesKeystore.THREAD_POST_ITEM_SHORT_INFO_HEIGHT_DEFAULT
    var commentMaxLines: Int = SharedPreferencesKeystore.COMMENT_MAX_MAX_LINES_DEFAULT
    var orientation: Int = -1
    var commentMarginWidth: Int = 0
    var commentTextSize: Int = SharedPreferencesKeystore.COMMENT_TEXT_SIZE_DEFAULT
    var commentTextPaint: TextPaint = TextPaint()
    var toolbarHeight: Int = 0

    override fun toString() = "imageWidthInDp: $imageWidthDp, " +
            "minImageHeightInDp: $minImageHeightDp, " +
            "maxImageHeightInDp: $maxImageHeightDp, " +
            "threadPostItemHorizontalWidth: $threadPostItemHorizontalWidth, " +
            "threadPostItemVerticalWidth: $threadPostItemVerticalWidth, " +
            "threadPostItemSingleImageHorizontalWidth: $threadPostItemSingleImageHorizontalWidth, " +
            "threadPostItemSingleImageVerticalWidth: $threadPostItemSingleImageVerticalWidth, " +
            "threadPostItemShortInfoHeight: $threadPostItemShortInfoHeight, " +
            "commentMaxLines: $commentMaxLines, " +
            "orientation: $orientation, " +
            "commentMarginWidth: $commentMarginWidth, " +
            "commentTextSize: $commentTextSize, " +
            "commentTextPaint: $commentTextPaint," +
            "toolbarHeight: $toolbarHeight"
}