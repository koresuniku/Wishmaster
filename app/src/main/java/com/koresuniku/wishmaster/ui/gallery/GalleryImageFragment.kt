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

import android.net.Uri
import android.os.Bundle
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
import com.koresuniku.wishmaster.core.data.network.Dvach
import com.koresuniku.wishmaster.application.global.WMImageUtils
import com.koresuniku.wishmaster.ui.base.BaseWishmasterFragment
import android.view.animation.Animation
import com.koresuniku.wishmaster.application.WishmasterApplication
import com.koresuniku.wishmaster.core.module.gallery.GalleryContract
import com.koresuniku.wishmaster.ui.utils.DeviceUtils
import javax.inject.Inject


/**
* Created by koresuniku on 3/3/18.
*/

class GalleryImageFragment : BaseWishmasterFragment(), GalleryContract.IGalleryItem {
    override lateinit var rootView: ViewGroup

    @BindView(R.id.clickable_view) lateinit var clickableView: View
    @BindView(R.id.preview) lateinit var preview: ImageView
    @BindView(R.id.images_container) lateinit var imagesContainer: View
    var bigImageView: BigImageView? = null

    @Inject lateinit var presenter: GalleryContract.IGalleryPresenter

    private var mPosition = -1
    private lateinit var mUrl: String

    init {
        (activity?.application as IGalleryActivity<*>)
                .galleryViewComponent
                .inject(this)
    }

    companion object {
        const val FRAGMENT_POSITION_KEY = "fragment_position"

        fun newInstance(position: Int):
                GalleryImageFragment {
            val fragment = GalleryImageFragment()
            val args = Bundle()
            args.putInt(FRAGMENT_POSITION_KEY, position)
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.gallery_image_layout, container, false) as ViewGroup
        ButterKnife.bind(this, rootView)

        bigImageView = rootView.findViewById(R.id.big_image_view)

        mPosition = arguments?.getInt(FRAGMENT_POSITION_KEY) ?: 0
        mUrl = arguments?.getString(presenter.getUrl()) ?: Dvach.BASE_URL

        presenter.getImageTargetCoordinates(mPosition, this)

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

    override fun onTargetDimensionsReady(coordinates: WMImageUtils.ImageCoordinates) {
        val previewImageCoordinates = presenter.previewCoordinates

        val imageWidth = DeviceUtils().getDisplayWidth(imagesContainer.context)
        val imageHeight = DeviceUtils().getDisplayHeight(imagesContainer.context)

        val scaleX = (previewImageCoordinates.xRight - previewImageCoordinates.xLeft).toFloat() /
                (coordinates.xRight - coordinates.xLeft).toFloat()
        val scaleY = (previewImageCoordinates.yBottom - previewImageCoordinates.yTop).toFloat() /
                (coordinates.yBottom - coordinates.yTop).toFloat()

        val imageCenterX = imageWidth / 2
        val imageCenterY = imageHeight / 2

        imagesContainer.scaleX = scaleX
        imagesContainer.scaleY = scaleY

        val translate = TranslateAnimation(
                Animation.ABSOLUTE, - (imageCenterX - previewImageCoordinates.xLeft -
                (0.5 * (previewImageCoordinates.xRight - previewImageCoordinates.xLeft))).toFloat(),
                Animation.ABSOLUTE, 0f,
                Animation.ABSOLUTE, - (imageCenterY - previewImageCoordinates.yTop -
                (0.5 * (previewImageCoordinates.yBottom - previewImageCoordinates.yTop))).toFloat(),
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        rootView.removeView(bigImageView)
        bigImageView = null
    }


}