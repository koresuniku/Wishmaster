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

package com.koresuniku.wishmaster.core.data.network.thread_list_api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.koresuniku.wishmaster.core.data.model.threads.Thread

/**
 * Created by koresuniku on 01.01.18.
 */

class ThreadListJsonSchemaPageResponse {

    @SerializedName("Board")
    @Expose
    lateinit var boardId: String

    @SerializedName("BoardName")
    @Expose
    lateinit var boardName: String

    @SerializedName("default_name")
    @Expose
    lateinit var defaultName: String

    @SerializedName("threads")
    @Expose
    lateinit var threads: MutableList<Thread>

    @SerializedName("pages")
    @Expose
    lateinit var pages: MutableList<Int>
}