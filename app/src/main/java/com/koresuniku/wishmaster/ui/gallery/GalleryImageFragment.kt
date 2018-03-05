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

import android.animation.ValueAnimator
import android.net.Uri
import android.os.Bundle
import android.support.annotation.Nullable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.*
import android.widget.ImageView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.github.piasy.biv.view.BigImageView
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.core.modules.gallery.IGalleryItem
import com.koresuniku.wishmaster.core.modules.gallery.IGalleryPresenter
import com.koresuniku.wishmaster.core.network.Dvach
import com.koresuniku.wishmaster.core.utils.images.WishmasterImageUtils
import com.koresuniku.wishmaster.ui.base.BaseWishmasterFragment
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.RelativeLayout
import com.koresuniku.wishmaster.ui.utils.DeviceUtils


/**
* Created by koresuniku on 3/3/18.
*/

class GalleryImageFragment : BaseWishmasterFragment(), IGalleryItem {
    override lateinit var rootView: ViewGroup

    @BindView(R.id.clickable_view) lateinit var clickableView: View
    @BindView(R.id.preview) lateinit var preview: ImageView
    @BindView(R.id.images_container) lateinit var imagesContainer: View
    var bigImageView: BigImageView? = null

    private lateinit var presenter: IGalleryPresenter

    private var mPosition = -1
    private lateinit var mUrl: String

    companion object {
        fun newInstance(presenter: IGalleryPresenter):
                GalleryImageFragment {
            val fragment = GalleryImageFragment()
            fragment.presenter = presenter
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.gallery_image_layout, container, false) as ViewGroup
        ButterKnife.bind(this, rootView)

        bigImageView = rootView.findViewById(R.id.big_image_view)

        mPosition = arguments?.getInt(GalleryPagerAdapter.FRAGMENT_POSITION_KEY) ?: 0
        mUrl = arguments?.getString(GalleryPagerAdapter.URL) ?: Dvach.BASE_URL

        presenter.getImageTargetCoordinates(mPosition, this)

        Log.d("GIF", "mUrl: $mUrl")
        Glide.with(preview.context)
                .load(Uri.parse("$mUrl${presenter.getFile(mPosition).thumbnail}"))
                .placeholder(preview.drawable)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(preview)

        bigImageView?.ssiv?.maxScale = 10.0f
        bigImageView?.ssiv?.resetScaleAndCenter()
        bigImageView?.showImage(Uri.parse("$mUrl${presenter.getFile(mPosition).path}"))

        return rootView
    }

    override fun onTargetDimensionsReady(coordinates: WishmasterImageUtils.ImageCoordinates) {
//        Log.d("WIU", "from: -> ${previewImageCoordinates.xLeft}:${previewImageCoordinates.yTop}" +
//                "|${previewImageCoordinates.xRight}:${previewImageCoordinates.yBottom}")
//        Log.d("WIU", "to: -> ${coordinates.xLeft}:${coordinates.yTop}" +
//                "|${coordinates.xRight}:${coordinates.yBottom}")

//        val scaleRatio = DeviceUtils().getDisplayWidth(imagesContainer.context).toFloat()
//                (coordinates.xRight - coordinates.xLeft).toFloat() /
//                        (previewImageCoordinates.xRight - previewImageCoordinates.xLeft).toFloat()

//

        val previewImageCoordinates = presenter.getPreviewImageCoordinates()

        val imageWidth = DeviceUtils().getDisplayWidth(imagesContainer.context)
        val imageHeight = DeviceUtils().getDisplayHeight(imagesContainer.context)

        var scaleX = (previewImageCoordinates.xRight - previewImageCoordinates.xLeft).toFloat() /
                (coordinates.xRight - coordinates.xLeft).toFloat()

        var scaleY = (previewImageCoordinates.yBottom - previewImageCoordinates.yTop).toFloat() /
                (coordinates.yBottom - coordinates.yTop).toFloat()



//        scaleX = 1 / scaleX
//        scaleY = 1 / scaleY

//        val previewCenterX = previewImageCoordinates.xLeft + (0.5 * previewImageCoordinates.xRight)
//        val previewCenterY = previewImageCoordinates.yTop

        val imageCenterX = imageWidth / 2
        val imageCenterY = imageHeight / 2

//        val xShift = (previewCenterX - imageCenterX).toFloat() / imageWidth.toFloat()
//        val yShift = (previewCenterY - imageCenterY).toFloat() / imageHeight.toFloat()
//
//

//        imagesContainer.post {
//            preview.translationX = xShift
//            preview.translationY = yShift
//            bigImageView?.translationX = xShift
//            bigImageView?.translationY = yShift
//            imagesContainer.requestLayout()
//        }
        Log.d("GIF", "scaleX: $scaleX, scaleY: $scaleY")

        imagesContainer.scaleX = scaleX
        imagesContainer.scaleY = scaleY



        val translate = TranslateAnimation(
                Animation.ABSOLUTE, -(imageCenterX - previewImageCoordinates.xLeft - (0.5 * (previewImageCoordinates.xRight-previewImageCoordinates.xLeft))).toFloat(),
                Animation.ABSOLUTE, 0f,
                Animation.ABSOLUTE, -(imageCenterY - previewImageCoordinates.yTop - (0.5 * (previewImageCoordinates.yBottom-previewImageCoordinates.yTop))).toFloat(),
                Animation.ABSOLUTE, 0f)
        translate.duration = resources.getInteger(R.integer.gallery_enter_duration).toLong()
        translate.duration = 5000
        translate.interpolator = AccelerateDecelerateInterpolator()

        imagesContainer.startAnimation(translate)

        imagesContainer.animate()
                .setDuration(resources.getInteger(R.integer.gallery_enter_duration).toLong())
                .setDuration(5000)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .scaleX(1f)
                .scaleY(1f)
                .start()

        //preview.startAnimation(scale)
        //bigImageView?.startAnimation(scale)

//        imagesContainer.layoutParams.width =
//                previewImageCoordinates.xRight - previewImageCoordinates.xLeft
//        imagesContainer.layoutParams.height =
//                previewImageCoordinates.yBottom - previewImageCoordinates.yTop
//        (imagesContainer.layoutParams as RelativeLayout.LayoutParams)
//                .setMargins(previewImageCoordinates.xLeft, previewImageCoordinates.yTop, 0, 0)



//        set.duration = 3000
//        set.interpolator = LinearInterpolator()
//        set.setAnimationListener(object : Animation.AnimationListener {
//            override fun onAnimationRepeat(p0: Animation?) {
//
//            }
//
//            override fun onAnimationEnd(p0: Animation?) {
//                (imagesContainer.layoutParams as RelativeLayout.LayoutParams)
//                        .setMargins(0, 0, 0, 0)
//            }
//
//            override fun onAnimationStart(p0: Animation?) {
//            }
//        })
//
//        val widthToResize = DeviceUtils().getDisplayWidth(imagesContainer.context)-
//                previewImageCoordinates.xRight - previewImageCoordinates.xLeft
//        val heightToResize = DeviceUtils().getDisplayHeight(imagesContainer.context) -
//                previewImageCoordinates.yBottom - previewImageCoordinates.yTop
//
//        val animator = ValueAnimator.ofFloat(0f, 1f)
//        animator.duration = 3000
//        animator.interpolator = LinearInterpolator()
//        animator.addUpdateListener {
//            val value = it.animatedValue as Float
//
//            val xShift: Float =  (previewImageCoordinates.xLeft.toFloat() * (1 - value))
//            val yShift: Float = (previewImageCoordinates.yTop.toFloat() * (1-value))
//
//            val width = previewImageCoordinates.xRight - previewImageCoordinates.xLeft +
//                    (widthToResize * value)
//            val height = previewImageCoordinates.yTop - previewImageCoordinates.yBottom +
//                    (heightToResize * value)
//
//            val params = (imagesContainer.layoutParams as RelativeLayout.LayoutParams)
//            params.setMargins(xShift.toInt(), yShift.toInt(), 0, 0)
//            params.width = width.toInt()
//            params.height = height.toInt()
//            imagesContainer.requestLayout()
//
//
//        }
//        animator.start()

//        val fromX = (previewImageCoordinates.xLeft.toFloat() /
//                DeviceUtils().getDisplayWidth(imagesContainer.context).toFloat())
//        val fromY = (previewImageCoordinates.yTop.toFloat() /
//                DeviceUtils().getDisplayHeight(imagesContainer.context).toFloat())
//
//        val translate = TranslateAnimation(
//                Animation.RELATIVE_TO_SELF, fromX,
//                Animation.RELATIVE_TO_SELF, 0f,
//                Animation.RELATIVE_TO_SELF, fromY,
//                Animation.RELATIVE_TO_SELF, 0f)
//        translate.duration = 3000
//        val anim = ScaleAnimation(
//                1f, scaleX, // Start and end values for the X axis scaling
//                1f, scaleY, // Start and end values for the Y axis scaling
//                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
//                Animation.RELATIVE_TO_SELF, 0.5f) // Pivot point of Y scaling
//        anim.fillAfter = true // Needed to keep the result of the animation
//        anim.duration = 3000
//        set.addAnimation(anim)
//        //set.addAnimation(translate)
//        imagesContainer.startAnimation(set)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        rootView.removeView(bigImageView)
        bigImageView = null
    }


}