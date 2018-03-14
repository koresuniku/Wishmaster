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

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import android.view.ViewGroup
import android.widget.ImageView
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.core.module.gallery.MediaTypeMatcher
import com.koresuniku.wishmaster.core.module.gallery.GalleryContract
import com.koresuniku.wishmaster.core.module.gallery.IGalleryViewComponent
import javax.inject.Inject

/**
 * Created by koresuniku on 2/28/18.
 */

class GalleryPagerAdapter(fragmentManager: FragmentManager, galleryViewComponent: IGalleryViewComponent) :
        FragmentStatePagerAdapter(fragmentManager) {

    @Inject lateinit var galleryPresenter: GalleryContract.IGalleryPresenter

    init { galleryViewComponent.inject(this) }

    override fun getItem(position: Int): Fragment {
        return when (galleryPresenter.matchFile(position)) {
            MediaTypeMatcher.MediaType.IMAGE -> GalleryImageFragment.newInstance(position)
            else -> Fragment()
        }
    }

    override fun getItemPosition(`object`: Any) = PagerAdapter.POSITION_NONE
    override fun getCount() = galleryPresenter.files.size
}