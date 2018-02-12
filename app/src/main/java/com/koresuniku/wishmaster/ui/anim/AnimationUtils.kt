package com.koresuniku.wishmaster.ui.anim

import android.os.Build
import android.support.annotation.RequiresApi
import android.support.design.widget.TabLayout
import android.support.v7.widget.Toolbar
import android.view.Window

/**
 * Created by koresuniku on 2/12/18.
 */

class AnimationUtils {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun setDashboardTransitions(window: Window, toolbar: Toolbar, tabLayout: TabLayout) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            val transitionExit = DashboardExitTransition(window.context, toolbar, tabLayout)
            transitionExit.excludeTarget(toolbar, true)
            transitionExit.excludeTarget(tabLayout, true)
            window.exitTransition = transitionExit

            val transitionEnter = DashboardEnterTransition(window.context, toolbar, tabLayout)
            transitionEnter.excludeTarget(toolbar, true)
            transitionEnter.excludeTarget(tabLayout, true)
            window.reenterTransition = transitionEnter
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun setThreadListTransitions(window: Window, toolbar: Toolbar) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            val transitionExit = ThreadListExitTransition(window.context, toolbar)
            transitionExit.excludeTarget(toolbar, true)
            window.reenterTransition = transitionExit

            val transitionEnter = ThreadListEnterTransition(window.context, toolbar)
            transitionEnter.excludeTarget(toolbar, true)
            window.reenterTransition = transitionEnter
        }
    }
}