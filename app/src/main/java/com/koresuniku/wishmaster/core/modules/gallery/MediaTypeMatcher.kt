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

package com.koresuniku.wishmaster.core.modules.gallery

import android.util.Log
import com.koresuniku.wishmaster.core.data.model.threads.File
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

/**
 * Created by koresuniku on 3/3/18.
 */

class MediaTypeMatcher @Inject constructor() {

    enum class MediaType { IMAGE, GIF, VIDEO, UNKNOWN }

    private val imageFormats = ArrayList(Arrays.asList("jpg", "jpeg", "png", "webp"))
    private val gifFormat = "gif"
    private val videoFormats = ArrayList(Arrays.asList("webm", "mp4"))

    fun matchFile(file: File): MediaType {
        val retrievedFormat = file.path.split(Regex("\\.")).last()

        retrievedFormat.let {
            return when {
                imageFormats.contains(it) -> MediaType.IMAGE
                it == gifFormat -> MediaType.GIF
                videoFormats.contains(it) -> MediaType.VIDEO
                else -> MediaType.UNKNOWN
            }
        }
    }
}