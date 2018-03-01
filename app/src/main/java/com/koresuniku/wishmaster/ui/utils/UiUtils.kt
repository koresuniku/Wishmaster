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

package com.koresuniku.wishmaster.ui.utils

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import com.koresuniku.wishmaster.R
import javax.inject.Inject




/**
 * Created by koresuniku on 03.10.17.
 */

class UiUtils @Inject constructor(private val deviceUtils: DeviceUtils) {

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

    private var isSystemUiShown: Boolean = true

    fun showSystemUI(activity: Activity) {
        if (deviceUtils.sdkIsKitkatOrHigher()) {
            isSystemUiShown = true
            activity.window.decorView.post {
                if (activity.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    activity.window.decorView.systemUiVisibility =
                            View.SYSTEM_UI_FLAG_FULLSCREEN or
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                            View.SYSTEM_UI_FLAG_IMMERSIVE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                }
                if (activity.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    if (deviceUtils.deviceHasNavigationBar(activity)) {
                        activity.window.decorView.systemUiVisibility =
                                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_FULLSCREEN
                    } else {
                        activity.window.decorView.systemUiVisibility =
                                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_FULLSCREEN
                    }
                }
            }
        }
    }

    fun showSystemUi(fakeStatusBar: View) {
        fakeStatusBar.layoutParams.height = 0
        fakeStatusBar.requestLayout()
    }

    fun hideSystemUI(activity: Activity) {
        if (deviceUtils.sdkIsKitkatOrHigher()) {
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

    fun setBarsTranslucent(activity: Activity, translucent: Boolean, fakeStatusBar: View) {
        setStatusBarTranslucent(activity, translucent)
        setNavigationBarTranslucent(activity, translucent)
//        if (translucent) {
//            val statusBarHeight = getStatusBarHeight(activity)
//            fakeStatusBar.layoutParams.height = statusBarHeight
//            fakeStatusBar.requestLayout()
//            fakeStatusBar.bringToFront()
//        } else {
//            fakeStatusBar.layoutParams.height = 0
//            fakeStatusBar.requestLayout()
//            fakeStatusBar.bringToFront()
//        }
    }

    fun setStatusBarTranslucent(activity: Activity, translucent: Boolean) {
        if (translucent) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN)
           // activity.window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        } else {
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN)
            //activity.window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }

    fun setNavigationBarTranslucent(activity: Activity, translucent: Boolean) {
        if (translucent) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN)
           // activity.window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        } else {
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN)
            //activity.window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        }
    }

    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier(
                "status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

//    fun setStatusBarTextColor(activity: Activity, state: StatusBarState) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            var newUiVisibility = activity.window.decorView.systemUiVisibility as Int
//
//            if (state === StatusBarState.Light) {
//                //Dark Text to show up on your light status bar
//                newUiVisibility = newUiVisibility or
//            } else if (state === StatusBarState.Dark) {
//                //Light Text to show up on your dark status bar
//                newUiVisibility = newUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
//            }
//
//            MyActivity.Window.DecorView.SystemUiVisibility = newUiVisibility as Android.Views.StatusBarVisibility
//        }
//
//
//    }
//
//    enum class StatusBarState {
//        Light,
//        Dark
//    }

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