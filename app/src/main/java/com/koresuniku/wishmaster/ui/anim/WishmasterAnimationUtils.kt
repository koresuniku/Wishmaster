package com.koresuniku.wishmaster.ui.anim

import android.animation.Animator
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.design.widget.TabLayout
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.Window
import android.widget.ImageView
import com.koresuniku.wishmaster.R
import javax.inject.Inject
import android.support.v7.widget.RecyclerView
import android.transition.Fade
import android.transition.Transition
import android.view.animation.*
import android.widget.TextView
import com.bumptech.glide.Glide
import com.koresuniku.wishmaster.ui.full_thread.FullThreadActivity


/**
 * Created by koresuniku on 2/12/18.
 */

class WishmasterAnimationUtils @Inject constructor() {

    fun showLoadingYoba(yoba: ImageView, loadingLayout: View) {
        yoba.post {
            loadingLayout.visibility = View.VISIBLE
            yoba.setLayerType(View.LAYER_TYPE_HARDWARE, null)
            val rotationAnimation = AnimationUtils.loadAnimation(yoba.context, R.anim.anim_rotate_infinitely)
            rotationAnimation.interpolator = LinearInterpolator()
            yoba.startAnimation(rotationAnimation)
        }
    }

    fun hideLoadingYoba(yoba: ImageView, loadingLayout: View) {
        yoba.post {
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
                .start() }
    }

    fun setSlideFromBottomLayoutAnimation(recyclerView: RecyclerView) {
        val context = recyclerView.context
        val controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_slide_from_bottom)
        recyclerView.layoutAnimation = controller
        recyclerView.layoutAnimationListener = object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {}
            override fun onAnimationEnd(p0: Animation?) {}
            override fun onAnimationStart(p0: Animation?) {}
        }
    }

    fun setSlideFromRightLayoutAnimation(recyclerView: RecyclerView, activity: FullThreadActivity) {
        val context = recyclerView.context
        val controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_slide_from_right)
        recyclerView.layoutAnimation = controller
        recyclerView.layoutAnimationListener = object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {}
            override fun onAnimationEnd(p0: Animation?) { Glide.with(activity).resumeRequests() }
            override fun onAnimationStart(p0: Animation?) { }
        }
    }

    fun setFadeOutLayoutAnimation(recyclerView: RecyclerView) {
        val context = recyclerView.context
        val controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_fade_out)
        recyclerView.layoutAnimation = controller
        recyclerView.layoutAnimationListener = object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {}
            override fun onAnimationEnd(p0: Animation?) {}
            override fun onAnimationStart(p0: Animation?) {}
        }
    }

    fun fadeOutView(view: View) {
        view.animate().alpha(1f).setInterpolator(LinearInterpolator()).setDuration(4000).start()
    }

    fun slideToLeft(view: View) {
        val slide = AnimationUtils.loadAnimation(view.context, R.anim.slide_to_left)
        slide.interpolator = AccelerateInterpolator()
        slide.duration = 300
        slide.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {}

            override fun onAnimationEnd(p0: Animation?) {
                //view.visibility = View.GONE
            }

            override fun onAnimationStart(p0: Animation?) {}
        })
        view.post { view.startAnimation(slide) }
    }

    fun slideToRight(view: View) {
        val slide = AnimationUtils.loadAnimation(view.context, R.anim.slide_to_right)
        slide.interpolator = AccelerateInterpolator()
        slide.duration = 300
        slide.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {}

            override fun onAnimationEnd(p0: Animation?) {
                //view.visibility = View.GONE
            }

            override fun onAnimationStart(p0: Animation?) {}
        })
        view.post { view.startAnimation(slide) }
    }

    fun slideFromLeft(view: View) {
        val slide = AnimationUtils.loadAnimation(view.context, R.anim.slide_from_left)
        slide.interpolator = AccelerateInterpolator()
        slide.duration = 150
        view.post { view.startAnimation(slide) }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun setDashboardTransitions(window: Window, toolbar: Toolbar, tabLayout: TabLayout) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            val transitionExit = DashboardExitTransition(window.context, toolbar, tabLayout,
                    this)
            transitionExit.excludeTarget(toolbar, true)
            transitionExit.excludeTarget(tabLayout, true)
            window.exitTransition = transitionExit

            val transitionReenter = DashboardReenterTransition(window.context, toolbar, tabLayout,
                    this)
            transitionReenter.excludeTarget(toolbar, true)
            transitionReenter.excludeTarget(tabLayout, true)
            window.reenterTransition = transitionReenter
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun setThreadListTransitions(window: Window, toolbar: Toolbar, recyclerView: RecyclerView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            window.exitTransition?.excludeTarget(toolbar, true)
            window.exitTransition?.excludeTarget(recyclerView, true)
            window.reenterTransition?.excludeTarget(toolbar, true)
            window.reenterTransition?.excludeTarget(recyclerView, true)
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun setFullThreadTransitions(window: Window, toolbar: Toolbar, recyclerView: RecyclerView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            window.enterTransition?.excludeTarget(toolbar, true)
            window.enterTransition?.excludeTarget(recyclerView, true)
            window.returnTransition?.excludeTarget(toolbar, true)
            window.returnTransition?.excludeTarget(recyclerView, true)
        }
    }

    fun fadeToolbar(fade: Boolean, toolbar: Toolbar) {
        (0 until toolbar.childCount)
                .map { toolbar.getChildAt(it) }
                .filter { it is TextView}
                .forEach {
                    it.animate().alpha(if (fade) 0f else 1f)
                            .setDuration(100)
                            .setInterpolator(LinearInterpolator())
                            .start()
                }
    }
}