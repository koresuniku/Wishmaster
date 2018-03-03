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
import com.koresuniku.wishmaster.core.modules.gallery.IGalleryPresenter
import com.koresuniku.wishmaster.core.modules.gallery.MediaTypeMatcher

/**
 * Created by koresuniku on 2/28/18.
 */

class GalleryPagerAdapter(fragmentManager: FragmentManager,
                          private val galleryPresenter: IGalleryPresenter,
                          private val mediaTypeMatcher: MediaTypeMatcher) :
        FragmentStatePagerAdapter(fragmentManager), IGalleryController {

    companion object {
        val FILE_POSITION_KEY = 0
    }

    override fun getItem(position: Int): Fragment {
        mediaTypeMatcher.matchFile(getFile(position))
        return GalleryFragment()
    }

    override fun getItemPosition(`object`: Any) = PagerAdapter.POSITION_NONE
    override fun getCount() = galleryPresenter.getGalleryState().fileListInList.size

    override fun onLayoutClicked() {}

    override fun getFile(position: Int) = galleryPresenter.getGalleryState().fileListInList[position]


}