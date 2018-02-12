package com.koresuniku.wishmaster.ui.anim

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.Context
import android.support.design.widget.TabLayout
import android.support.v7.widget.Toolbar
import android.transition.Fade
import android.transition.TransitionValues
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.TextView
import com.koresuniku.wishmaster.R
import java.lang.ref.WeakReference

@SuppressLint("NewApi")
/**
 * Created by koresuniku on 2/12/18.
 */

class DashboardEnterTransition(context: Context,
                               toolbar: Toolbar,
                               tabLayout: TabLayout) : Fade() {

    private val contextReference = WeakReference(context)
    private val toolbarReference = WeakReference(toolbar)
    private val tabLayoutReference = WeakReference(tabLayout)

    override fun getDuration() = contextReference.get()
            ?.resources
            ?.getInteger(R.integer.default_transition_duration)
            ?.toLong()
            ?: 0

    override fun getInterpolator() = AccelerateDecelerateInterpolator()

    override fun onAppear(sceneRoot: ViewGroup?, startValues: TransitionValues?, startVisibility: Int, endValues: TransitionValues?, endVisibility: Int): Animator {
        animateTabLayout()
        animateToolbar()
        return super.onAppear(sceneRoot, startValues, startVisibility, endValues, endVisibility)
    }

    private fun animateTabLayout() {
        val translateUpwards = TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, -1f,
                Animation.RELATIVE_TO_SELF, 0f)
        translateUpwards.duration = (duration * 1f).toLong()
        translateUpwards.interpolator = interpolator
        translateUpwards.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {}
            override fun onAnimationEnd(p0: Animation?) {  }
            override fun onAnimationStart(p0: Animation?) {
                tabLayoutReference.get()?.visibility = View.VISIBLE
                toolbarReference.get()?.bringToFront()
            }
        })
        tabLayoutReference.get()?.startAnimation(translateUpwards)
    }

    private fun animateToolbar() {
        toolbarReference.get()?.let {
            for (i in 0 until it.childCount) {
                val child = it.getChildAt(i)
                child.alpha = 0f
                child.animate().alpha(1f)
                        .setDuration(duration)
                        .setInterpolator(interpolator)
                        .start()
            }
        }
    }
}