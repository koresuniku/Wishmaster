package com.koresuniku.wishmaster.core.modules.gallery

/**
 * Created by koresuniku on 14.01.18.
 */

data class ImageLayoutDimensions(val widthInPx: Int, val heightInPx: Int) {
    override fun toString(): String = "widthInPx: $widthInPx, heightInPx: $heightInPx"
}