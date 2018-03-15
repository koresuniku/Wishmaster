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

package com.koresuniku.wishmaster.core.data.network.full_thread_api

import com.koresuniku.wishmaster.core.data.model.posts.PostListData
import com.koresuniku.wishmaster.core.data.model.threads.File
import javax.inject.Inject

/**
 * Created by koresuniku on 3/15/18.
 */

class FullThreadResponseParser @Inject constructor() {

    fun obtainFileList(postListData: PostListData): MutableList<File> {
        val result = arrayListOf<File>()

        postListData.postList.forEach { it.files?.let { result.addAll(it) } }

        return result
    }
}