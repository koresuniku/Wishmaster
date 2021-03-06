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

package com.koresuniku.wishmaster.application.global

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

class WMAnimationUtils @Inject constructor() {

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
            override fun onAnimationEnd(p0: Animation?) { }
            override fun onAnimationStart(p0: Animation?) { }
        }
    }

}