package com.koresuniku.wishmaster.ui.anim

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.Toolbar
import android.transition.Fade
import android.transition.Slide
import android.transition.TransitionValues
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.widget.TextView
import com.koresuniku.wishmaster.R
import java.lang.ref.WeakReference

@SuppressLint("NewApi")
/**
 * Created by koresuniku on 2/12/18.
 */

class ThreadListExitTransition(context: Context,
                               toolbar: Toolbar,
                               private val wishmasterAnimationUtils: WishmasterAnimationUtils,
                               gravity: Int) : Slide(gravity) {

    private val contextReference = WeakReference(context)
    private val toolbarReference = WeakReference(toolbar)

    override fun getDuration() = contextReference.get()
            ?.resources
            ?.getInteger(R.integer.default_transition_duration)
            ?.toLong()
            ?: 0


    override fun getInterpolator() = AccelerateInterpolator()

    override fun onAppear(sceneRoot: ViewGroup?, view: View?, startValues: TransitionValues?, endValues: TransitionValues?): Animator {
        //toolbarReference.get()?.let { wishmasterAnimationUtils.fadeToolbar(true, it, duration, interpolator) }
        return super.onDisappear(sceneRoot, view, startValues, endValues)
    }
}