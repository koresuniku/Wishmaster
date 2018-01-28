package com.koresuniku.wishmaster_v4.ui.util

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.PorterDuff
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import com.koresuniku.wishmaster_v4.R
import kotlin.math.roundToInt

/**
 * Created by koresuniku on 03.10.17.
 */

object UiUtils {

    fun setImageViewColorFilter(imageView: ImageView, color: Int) {
        imageView.setColorFilter(
                imageView.resources.getColor(color),
                PorterDuff.Mode.SRC_ATOP)
    }

    fun convertPixelsToDp(px: Float): Float {
        val metrics = Resources.getSystem().displayMetrics
        val dp = Math.floor((px / (metrics.densityDpi / 160f)).toDouble())
        return Math.round(dp).toFloat()
    }

    fun convertDpToPixel(dp: Float): Float {
        val metrics = Resources.getSystem().displayMetrics
        val px = Math.floor((dp * (metrics.densityDpi / 160f)).toDouble())
        return Math.round(px).toFloat()
    }

    var isSystemUiShown: Boolean = true

    fun showSystemUI(activity: Activity) {
        if (DeviceUtils.sdkIsKitkatOrHigher()) {
            isSystemUiShown = true
            if (activity.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                activity.window.decorView.systemUiVisibility =
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            }
            if (activity.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                if (DeviceUtils.deviceHasNavigationBar(activity)) {
                    activity.window.decorView.systemUiVisibility =
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                } else {
                    activity.window.decorView.systemUiVisibility =
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                }
            }
        }
    }

    fun hideSystemUI(activity: Activity) {
        if (DeviceUtils.sdkIsKitkatOrHigher()) {
            isSystemUiShown = false
            activity.window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                            View.SYSTEM_UI_FLAG_FULLSCREEN or
                            View.SYSTEM_UI_FLAG_IMMERSIVE
        }
    }

    fun setBarsTranslucent(activity: Activity, translucent: Boolean) {
        setStatusBarTranslucent(activity, translucent)
        setNavigationBarTranslucent(activity, translucent)
    }

    private fun setStatusBarTranslucent(activity: Activity, translucent: Boolean) {
        if (translucent) {
            // activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        } else {
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }

    private fun setNavigationBarTranslucent(activity: Activity, translucent: Boolean) {
        if (translucent) {
            //activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        } else {
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        }
    }

    fun getDefaultImageWidthInDp(screenWidth: Int, context: Context): Int {
        val sideMargin = context.resources.getDimension(R.dimen.thread_post_side_margin_default)
        Log.d("UIU", "screenWidth: $screenWidth, sideMargin: $sideMargin")
        val rawFloat = (screenWidth - (5 * sideMargin)) / 4
        Log.d("UIU", "rawFloat: $rawFloat")
        val afterConvert = convertPixelsToDp(Math.ceil(rawFloat.toDouble()).toFloat()).toInt()
        Log.d("UIU", "after: $afterConvert")
        return convertPixelsToDp(Math.ceil(rawFloat.toDouble()).toFloat()).toInt()
    }

}