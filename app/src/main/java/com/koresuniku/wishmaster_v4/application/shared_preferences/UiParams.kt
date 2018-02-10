package com.koresuniku.wishmaster_v4.application.shared_preferences

import android.text.TextPaint
import javax.inject.Inject


class UiParams @Inject constructor() : IUiParams {
    override var imageWidthDp: Int = SharedPreferencesKeystore.DEFAULT_IMAGE_WIDTH_IN_DP_DEFAULT
    override var minImageHeightDp: Int = SharedPreferencesKeystore.MINIMUM_IMAGE_HEIGHT_IN_DP_DEFAULT
    override var maxImageHeightDp: Int = SharedPreferencesKeystore.MAXIMUM_IMAGE_HEIGHT_IN_DP_DEFAULT
    override var threadPostItemHorizontalWidth: Int = SharedPreferencesKeystore.THREAD_POST_ITEM_WIDTH_DEFAULT
    override var threadPostItemVerticalWidth: Int = SharedPreferencesKeystore.THREAD_POST_ITEM_WIDTH_DEFAULT
    override var threadPostItemSingleImageHorizontalWidth: Int = SharedPreferencesKeystore.THREAD_POST_ITEM_WIDTH_SINGLE_IMAGE_DEFAULT
    override var threadPostItemSingleImageVerticalWidth: Int = SharedPreferencesKeystore.THREAD_POST_ITEM_WIDTH_SINGLE_IMAGE_DEFAULT
    override var threadPostItemShortInfoHeight: Int = SharedPreferencesKeystore.THREAD_POST_ITEM_SHORT_INFO_HEIGHT_DEFAULT
    override var commentMaxLines: Int = SharedPreferencesKeystore.COMMENT_MAX_MAX_LINES_DEFAULT
    override var orientation: Int = -1
    override var commentMarginWidth: Int = 0
    override var commentTextSize: Int = SharedPreferencesKeystore.COMMENT_TEXT_SIZE_DEFAULT
    override var commentTextPaint: TextPaint = TextPaint()

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
            "commentTextPaint: $commentTextPaint"
}