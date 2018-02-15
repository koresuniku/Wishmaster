package com.koresuniku.wishmaster.ui.anim

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.Toolbar
import android.transition.Slide
import android.transition.TransitionValues
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import java.lang.ref.WeakReference

@SuppressLint("NewApi")
/**
 * Created by koresuniku on 2/13/18.
 */

class FullThreadEnterTransition(context: Context,
                                toolbar: Toolbar,
                                private val wishmasterAnimationUtils: WishmasterAnimationUtils,
                                gravity: Int) : Slide(gravity) {

    private val contextReference = WeakReference(context)
    private val toolbarReference = WeakReference(toolbar)

    override fun getDuration() = 200.toLong()

    override fun getInterpolator() = AccelerateInterpolator()

    override fun onAppear(sceneRoot: ViewGroup?, view: View?, startValues: TransitionValues?,
                          endValues: TransitionValues?): Animator {
        //toolbarReference.get()?.let { wishmasterAnimationUtils.fadeToolbar(false, it, duration, interpolator) }
        return super.onDisappear(sceneRoot, view, startValues, endValues)
    }
}