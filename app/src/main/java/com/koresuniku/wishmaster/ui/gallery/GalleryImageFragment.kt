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
import android.support.annotation.Nullable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

/**
* Created by koresuniku on 3/3/18.
*/

class GalleryImageFragment : BaseWishmasterFragment(), IGalleryItem {
    override lateinit var rootView: ViewGroup

    @BindView(R.id.clickable_view) lateinit var clickableView: View
    @BindView(R.id.preview) lateinit var preview: ImageView
    var bigImageView: BigImageView? = null

    private lateinit var presenter: IGalleryPresenter

    private var mPosition = -1
    private lateinit var mUrl: String

    companion object {
        fun newInstance(presenter: IGalleryPresenter): GalleryImageFragment {
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

    override fun onTargetDimensionsReady(dimensions: WishmasterImageUtils.ImageTargetDimensions) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        rootView.removeView(bigImageView)
        bigImageView = null
    }


}