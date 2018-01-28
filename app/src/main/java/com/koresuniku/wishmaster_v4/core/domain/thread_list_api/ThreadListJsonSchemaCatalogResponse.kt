package com.koresuniku.wishmaster_v4.core.domain.thread_list_api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.koresuniku.wishmaster_v4.core.data.threads.Thread

/**
 * Created by koresuniku on 01.01.18.
 */

class ThreadListJsonSchemaCatalogResponse {

    @SerializedName("BoardName")
    @Expose
    lateinit var boardName: String

    @SerializedName("default_name")
    @Expose
    lateinit var defaultName: String

    @SerializedName("threads")
    @Expose
    lateinit var threads: MutableList<Thread>
}