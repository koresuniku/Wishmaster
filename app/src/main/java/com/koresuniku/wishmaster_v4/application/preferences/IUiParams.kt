package com.koresuniku.wishmaster_v4.application.preferences

import android.text.TextPaint


interface IUiParams {
    var imageWidthDp: Int
    var minImageHeightDp: Int
    var maxImageHeightDp: Int
    var threadPostItemHorizontalWidth: Int
    var threadPostItemVerticalWidth: Int
    var threadPostItemSingleImageHorizontalWidth: Int
    var threadPostItemSingleImageVerticalWidth: Int
    var threadPostItemShortInfoHeight: Int
    var commentMaxLines: Int
    var orientation: Int
    var commentMarginWidth: Int
    var commentTextSize: Int
    var commentTextPaint: TextPaint
}