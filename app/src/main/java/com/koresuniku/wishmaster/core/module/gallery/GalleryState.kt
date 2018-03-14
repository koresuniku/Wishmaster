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

package com.koresuniku.wishmaster.core.module.gallery

import android.graphics.drawable.Drawable
import com.koresuniku.wishmaster.application.global.WMImageUtils
import javax.inject.Inject

/**
 * Created by koresuniku on 3/2/18.
 */

class GalleryState @Inject constructor() {

    var currentFilePosition: Int = 0
    var currentPostPosition: Int = 0

    var previewClickedPosition: Int = 0
    var previewAnimated: Boolean = false
    var previewDrawable: Drawable? = null
    var previewCoordinates: WMImageUtils.ImageCoordinates? = null

    fun resetState() {
        currentFilePosition = 0
        currentPostPosition = 0
        previewClickedPosition = 0
        previewAnimated = false
        previewDrawable = null
        previewCoordinates = null
    }
}