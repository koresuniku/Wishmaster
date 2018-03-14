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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.application.WishmasterApplication
import com.koresuniku.wishmaster.core.module.gallery.GalleryContract
import com.koresuniku.wishmaster.core.module.gallery.IGalleryViewComponent
import com.koresuniku.wishmaster.ui.base.BaseWishmasterFragment
import javax.inject.Inject

/**
 * Created by koresuniku on 13.03.18.
 */

class GalleryFragment : BaseWishmasterFragment(), GalleryContract.IGalleryMainView {

    override lateinit var rootView: ViewGroup

    @Inject lateinit var presenter: GalleryContract.IGalleryPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_gallery, container, false) as ViewGroup


        (activity?.application as IGalleryActivity<*>)
                .galleryViewComponent
                .inject(this)

        return rootView
    }

    override var isGalleryOpened = false

    override fun openGallery() {}

    override fun closeGallery() {}
}