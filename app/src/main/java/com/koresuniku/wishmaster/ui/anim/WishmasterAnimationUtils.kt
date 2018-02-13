package com.koresuniku.wishmaster.ui.anim

import android.animation.Animator
import android.os.Build
import android.os.Handler
import android.support.annotation.RequiresApi
import android.support.design.widget.TabLayout
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.Window
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import com.koresuniku.wishmaster.R
import javax.inject.Inject
import android.support.v7.widget.RecyclerView
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.LayoutAnimationController







/**
 * Created by koresuniku on 2/12/18.
 */

class WishmasterAnimationUtils @Inject constructor() {

    fun showLoadingYoba(yoba: ImageView, loadingLayout: View) {
        loadingLayout.visibility = View.VISIBLE
        yoba.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        val rotationAnimation = AnimationUtils.loadAnimation(yoba.context, R.anim.anim_rotate_infinitely)
        yoba.startAnimation(rotationAnimation)
    }

    fun hideLoadingYoba(yoba: ImageView, loadingLayout: View) {
        yoba.animate()
                .alpha(0f)
                .setInterpolator(LinearInterpolator())
                .setDuration(yoba.context.resources.getInteger(R.integer.yoba_disappear_duration).toLong())
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationEnd(p0: Animator?) {
                        yoba.clearAnimation()
                        yoba.setLayerType(View.LAYER_TYPE_NONE, null)
                        loadingLayout.visibility = View.GONE
                        yoba.alpha = 1f
                    }
                    override fun onAnimationRepeat(p0: Animator?) {}
                    override fun onAnimationCancel(p0: Animator?) {}
                    override fun onAnimationStart(p0: Animator?) {}
                })
                .start()
    }

    fun setLayoutAnimation(recyclerView: RecyclerView) {
        val context = recyclerView.context
        val controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_slide_from_bottom)
        recyclerView.layoutAnimation = controller
    }

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