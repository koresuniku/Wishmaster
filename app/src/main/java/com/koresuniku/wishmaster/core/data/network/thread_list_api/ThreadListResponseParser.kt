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

import com.koresuniku.wishmaster.core.data.model.posts.Post
import com.koresuniku.wishmaster.core.data.model.threads.Thread
import com.koresuniku.wishmaster.core.data.model.threads.ThreadListData
import javax.inject.Inject

/**
 * Created by koresuniku on 01.01.18.
 */

class ThreadListResponseParser @Inject constructor() {

    fun mapCatalogResponseToThreadListData(schemaCatalog: ThreadListJsonSchemaCatalogResponse): ThreadListData {
        val threadListData = ThreadListData()

        threadListData.setBoardId(schemaCatalog.boardId)
        threadListData.setBoardName(schemaCatalog.boardName)
        threadListData.setDefaultName(schemaCatalog.defaultName)
        threadListData.setBoardList(schemaCatalog.threads)

        return threadListData
    }

    fun mapPageResponseToThreadListData(schemaPage: ThreadListJsonSchemaPageResponse): ThreadListData {
        val threadListData = ThreadListData()

        schemaPage.threads.forEach({
            val thread = it
            it.posts?.let { assignPostAttributesToThreadModel(thread, it[0]) }
        })

        threadListData.setBoardId(schemaPage.boardId)
        threadListData.setBoardName(schemaPage.boardName)
        threadListData.setDefaultName(schemaPage.defaultName)
        threadListData.setBoardList(schemaPage.threads)
        threadListData.setPagesCount(schemaPage.pages.size)

        return threadListData
    }

    private fun assignPostAttributesToThreadModel(thread: Thread, post: Post) {
        thread.subject = post.subject
        thread.comment = post.comment
        thread.files = post.files ?: emptyList()
        thread.date = post.date
        thread.num = post.num
        thread.trip = post.trip
        thread.name = post.name
    }
}