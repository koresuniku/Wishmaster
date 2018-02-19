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
import com.koresuniku.wishmaster.core.data.model.posts.Post

/**
 * Created by koresuniku on 01.01.18.
 */

class Thread {

    @SerializedName("comment")
    @Expose
    var comment: String? = null

    @SerializedName("date")
    @Expose
    lateinit var date: String

    @SerializedName("files")
    @Expose
    var files: List<File>? = null

    @SerializedName("name")
    @Expose
    lateinit var name: String

    @SerializedName("num")
    @Expose
    lateinit var num: String

    @SerializedName("trip")
    @Expose
    lateinit var trip: String

    @SerializedName("subject")
    @Expose
    var subject: String? = null

    @SerializedName("posts")
    @Expose
    var posts: MutableList<Post>? = null

    @SerializedName("posts_count")
    @Expose
    var postsCount: Int = 0

    @SerializedName("files_count")
    @Expose
    var filesCount: Int = 0
}