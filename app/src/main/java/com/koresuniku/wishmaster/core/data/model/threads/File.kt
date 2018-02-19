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

package com.koresuniku.wishmaster.core.data.model.threads

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by koresuniku on 01.01.18.
 */

class File {

    @SerializedName("height")
    @Expose
    lateinit var height: String

    @SerializedName("width")
    @Expose
    lateinit var width: String

    @SerializedName("path")
    @Expose
    lateinit var path: String

    @SerializedName("thumbnail")
    @Expose
    lateinit var thumbnail: String

    @SerializedName("size")
    @Expose
    lateinit var size: String

    @SerializedName("displayname")
    @Expose
    lateinit var displayName: String

    @SerializedName("duration")
    @Expose
    lateinit var duration: String
}