package com.koresuniku.wishmaster.ui.anim

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.Toolbar
import android.transition.Fade
import android.transition.TransitionValues
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import com.koresuniku.wishmaster.R
import java.lang.ref.WeakReference

@SuppressLint("NewApi")
/**
 * Created by koresuniku on 2/12/18.
 */

class ThreadListExitTransition(context: Context,
                               toolbar: Toolbar) : Fade() {

    private val contextReference = WeakReference(context)
    private val toolbarReference = WeakReference(toolbar)

    override fun getDuration() = contextReference.get()
            ?.resources
            ?.getInteger(R.integer.default_transition_duration)
            ?.toLong()
            ?: 0

    override fun getInterpolator() = AccelerateDecelerateInterpolator()

    override fun onAppear(sceneRoot: ViewGroup?, startValues: TransitionValues?, startVisibility: Int, endValues: TransitionValues?, endVisibility: Int): Animator {
        animateToolbar()
        return super.onAppear(sceneRoot, startValues, startVisibility, endValues, endVisibility)
    }

    private fun animateToolbar() {
        toolbarReference.get()?.let {
            for (i in 0 until it.childCount) {
                val child = it.getChildAt(i)
                child.animate().alpha(0f)
                        .setDuration(duration)
                        .setInterpolator(interpolator)
                        .start()
            }
        }
    }
}