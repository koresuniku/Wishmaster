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
import butterknife.BindView
import butterknife.ButterKnife
import com.github.piasy.biv.view.BigImageView
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.core.data.model.threads.File
import com.koresuniku.wishmaster.ui.base.BaseWishmasterFragment

/**
 * Created by koresuniku on 3/3/18.
 */

class GalleryImageFragment : BaseWishmasterFragment() {
    override lateinit var rootView: View

    @BindView(R.id.clickable_view) lateinit var clickableView: View
    @BindView(R.id.big_image_view)lateinit var bigImageView: BigImageView

    private lateinit var file: File

    companion object {
        fun newInstance(): GalleryImageFragment {
            val args = Bundle()

            return GalleryImageFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.gallery_image_layout, container, false)

        ButterKnife.bind(this, rootView)

        return rootView
    }
}