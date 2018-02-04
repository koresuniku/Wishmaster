package com.koresuniku.wishmaster_v4.application.shared_preferences

import javax.inject.Inject


class SharedPreferencesUiDimens @Inject constructor() : ISharedPreferencesUiDimens {
    override var imageWidth: Int = SharedPreferencesKeystore.DEFAULT_IMAGE_WIDTH_IN_DP_DEFAULT
    override var minImageHeight: Int = SharedPreferencesKeystore.MINIMUM_IMAGE_HEIGHT_IN_DP_DEFAULT
    override var maxImageHeight: Int = SharedPreferencesKeystore.MAXIMUM_IMAGE_HEIGHT_IN_DP_DEFAULT
    override var threadPostItemHorizontalWidth: Int = SharedPreferencesKeystore.THREAD_POST_ITEM_WIDTH_DEFAULT
    override var threadPostItemVerticalWidth: Int = SharedPreferencesKeystore.THREAD_POST_ITEM_WIDTH_DEFAULT
    override var threadPostItemSingleImageHorizontalWidth: Int = SharedPreferencesKeystore.THREAD_POST_ITEM_WIDTH_SINGLE_IMAGE_DEFAULT
    override var threadPostItemSingleImageVerticalWidth: Int = SharedPreferencesKeystore.THREAD_POST_ITEM_WIDTH_SINGLE_IMAGE_DEFAULT
//
//    constructor(iw: Int, mih: Int, Mih: Int, hw: Int, vw: Int, sihv: Int, sivw: Int): this() {
//        this.imageWidth = iw
//        this.minImageHeight = mih
//        this.maxImageHeight = Mih
//        this.threadPostItemHorizontalWidth = hw
//        this.threadPostItemVerticalWidth = vw
//        this.threadPostItemSingleImageHorizontalWidth = sihv
//        this.threadPostItemSingleImageVerticalWidth = sivw
//    }

    override fun toString(): String {
        return "imageWidthInDp: $imageWidth, " +
                "minImageHeightInDp: $minImageHeight, " +
                "maxImageHeightInDp: $maxImageHeight, " +
                "threadPostItemHorizontalWidth: $threadPostItemHorizontalWidth, " +
                "threadPostItemVerticalWidth: $threadPostItemVerticalWidth, " +
                "threadPostItemSingleImageHorizontalWidth: $threadPostItemSingleImageHorizontalWidth, " +
                "threadPostItemSingleImageVerticalWidth: $threadPostItemSingleImageVerticalWidth"
    }
}