package com.koresuniku.wishmaster.application.preferences

import android.text.TextPaint
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