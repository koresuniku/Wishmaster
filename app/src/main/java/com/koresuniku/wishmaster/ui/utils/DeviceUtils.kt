package com.koresuniku.wishmaster.ui.utils

import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.view.KeyCharacterMap
import android.view.KeyEvent
import android.view.ViewConfiguration
import android.view.WindowManager

/**
 * Created by koresuniku on 03.10.17.
 */

class DeviceUtils {

    fun deviceHasNavigationBar(context: Context): Boolean {
        val hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey()
        val hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)
        return !hasMenuKey && !hasBackKey
    }

    fun sdkIsLollipopOrHigher(): Boolean = Build.VERSION.SDK_INT >= 20

    fun sdkIsKitkatOrHigher(): Boolean = Build.VERSION.SDK_INT >= 19

    fun sdkIsMarshmallowOrHigher(): Boolean = Build.VERSION.SDK_INT >= 23

    fun getDisplayWidth(context: Context): Int {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val metrics = DisplayMetrics()
        display.getMetrics(metrics)
        return metrics.widthPixels
    }

    fun getDisplayHeight(context: Context): Int {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val metrics = DisplayMetrics()
        display.getMetrics(metrics)
        return metrics.heightPixels
    }

    fun getActualDisplayWidthInPx(context: Context): Int {
        val width = context.resources.displayMetrics.widthPixels
        val height = context.resources.displayMetrics.heightPixels
        return if (height > width) width else height
    }
}