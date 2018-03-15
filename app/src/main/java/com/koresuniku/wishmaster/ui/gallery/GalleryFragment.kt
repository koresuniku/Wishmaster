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

package com.koresuniku.wishmaster.ui.gallery

import android.animation.Animator
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import butterknife.BindView
import butterknife.ButterKnife
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.core.module.gallery.GalleryContract
import com.koresuniku.wishmaster.ui.base.BaseWishmasterActivity
import com.koresuniku.wishmaster.ui.base.BaseWishmasterFragment
import com.koresuniku.wishmaster.ui.base.OnBackPressedListener
import com.koresuniku.wishmaster.ui.utils.UiUtils
import javax.inject.Inject

/**
 * Created by koresuniku on 13.03.18.
 */

class GalleryFragment : BaseWishmasterFragment(), GalleryContract.IGalleryMainView, OnBackPressedListener {

    override lateinit var rootView: ViewGroup
    @BindView(R.id.gallery_layout) lateinit var mGalleryLayout: View
    @BindView(R.id.gallery_background) lateinit var mGalleryBackground: View
    @BindView(R.id.gallery_view_pager) lateinit var mGalleryViewPager: ViewPager

    @Inject lateinit var presenter: GalleryContract.IGalleryPresenter
    @Inject lateinit var uiUtils: UiUtils

    override var isGalleryOpened = false
    private lateinit var mGalleryPagerAdapter: GalleryPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as IGalleryActivity<*>)
                .galleryViewComponent
                .inject(this)
        (activity as BaseWishmasterActivity).onBackPressedListener = this

        presenter.bindView(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.gallery_layout, container, false) as ViewGroup
        ButterKnife.bind(this, rootView)

        setupViewPager()

        return rootView
    }

    //private var yCoordinate = 0f
    private fun setupViewPager() {
        activity?.let {
            mGalleryLayout.visibility = View.GONE

            Log.d("GF", "setupViewPager: ${this.hashCode()}")
//            mGalleryLayout.setOnTouchListener { view, motionEvent ->
//                when(motionEvent.action) {
//                    MotionEvent.ACTION_DOWN -> {
//                        yCoordinate = view.y - motionEvent.rawY
//                    }
//                    MotionEvent.ACTION_MOVE -> {
//                        view.animate().y(motionEvent.rawY + yCoordinate).setDuration(0).start()
//                    }
//                    MotionEvent.ACTION_UP -> {
//                        //your stuff
//                    }
//                    else -> {}
//                }
//                true
//            }
        }
    }

    override fun doBack(): Boolean {
        return if (isGalleryOpened) {
            if (presenter.galleryState.isGalleryFullscreen) presenter.onGalleryLayoutClick()
            else closeGallery()
            false
        } else {
            true
        }
    }

    override fun hideSystemUi() { activity?.let { uiUtils.hideSystemUI(it) } }
    override fun showSystemUi() { activity?.let { uiUtils.showSystemUI(it) } }

    override fun openGallery() {
        activity?.let {
            isGalleryOpened = true

            Log.d("GF", "openGallery")
            mGalleryPagerAdapter = GalleryPagerAdapter(
                    it.supportFragmentManager,
                    (it as IGalleryActivity<*>).galleryViewComponent)
            mGalleryViewPager.adapter = mGalleryPagerAdapter
            mGalleryViewPager.setCurrentItem(presenter.galleryState.currentFilePositionGlobal, false)
            mGalleryLayout.visibility = View.VISIBLE

            mGalleryBackground.alpha = 0f
            mGalleryBackground.animate()
                    .alpha(1f)
                    .setDuration(resources.getInteger(R.integer.gallery_enter_duration).toLong())
                    .setInterpolator(LinearInterpolator())
                    .setListener(object : Animator.AnimatorListener {
                        override fun onAnimationRepeat(p0: Animator?) {}
                        override fun onAnimationEnd(p0: Animator?) {}
                        override fun onAnimationCancel(p0: Animator?) {}
                        override fun onAnimationStart(p0: Animator?) {
                            uiUtils.setBarsTranslucent(it, true)
                        }
                    })
                    .start()
        }
    }

    override fun closeGallery() {
        activity?.let {

            mGalleryLayout.animate()
                    .alpha(0f)
                    .setDuration(resources.getInteger(R.integer.gallery_exit_duration).toLong())
                    .setInterpolator(LinearInterpolator())
                    .setListener(object : Animator.AnimatorListener {
                        override fun onAnimationRepeat(p0: Animator?) {}
                        override fun onAnimationEnd(p0: Animator?) {
                            presenter.galleryState.resetState()
                            presenter.resetGallery()
                            mGalleryPagerAdapter.notifyDataSetChanged()
                            uiUtils.setBarsTranslucent(it, false)
                            mGalleryLayout.visibility = View.GONE
                            mGalleryLayout.alpha = 1f
                        }
                        override fun onAnimationCancel(p0: Animator?) {}
                        override fun onAnimationStart(p0: Animator?) {}
                    })
                    .start()

            isGalleryOpened = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unbindView()
    }
}