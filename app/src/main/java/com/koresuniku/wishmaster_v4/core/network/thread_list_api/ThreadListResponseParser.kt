package com.koresuniku.wishmaster_v4.core.network.thread_list_api

import com.koresuniku.wishmaster_v4.core.data.model.single_thread.Post
import com.koresuniku.wishmaster_v4.core.data.model.threads.Thread
import com.koresuniku.wishmaster_v4.core.data.model.threads.ThreadListData
import javax.inject.Inject

/**
 * Created by koresuniku on 01.01.18.
 */

class ThreadListResponseParser @Inject constructor() {

    fun mapCatalogResponseToThreadListData(schemaCatalog: ThreadListJsonSchemaCatalogResponse): ThreadListData {
        val threadListData = ThreadListData()

        threadListData.setBoardName(schemaCatalog.boardName)
        threadListData.setDefaultName(schemaCatalog.defaultName)
        threadListData.setBoardList(schemaCatalog.threads)

        return threadListData
    }

    fun mapPageResponseToThreadListData(schemaCatalog: ThreadListJsonSchemaPageResponse): ThreadListData {
        val threadListData = ThreadListData()

        schemaCatalog.threads.forEach({
            val thread = it
            it.posts?.let { assignPostAttributesToThreadModel(thread, it[0]) }
        })

        threadListData.setBoardName(schemaCatalog.boardName)
        threadListData.setDefaultName(schemaCatalog.defaultName)
        threadListData.setBoardList(schemaCatalog.threads)
        threadListData.setPagesCount(schemaCatalog.pages.size)

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