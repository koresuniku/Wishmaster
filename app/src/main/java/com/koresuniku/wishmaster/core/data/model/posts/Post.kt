package com.koresuniku.wishmaster.core.data.model.posts

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.koresuniku.wishmaster.core.data.model.threads.File

/**
 * Created by koresuniku on 13.01.18.
 */

class Post {
    @SerializedName("comment")
    @Expose
    var comment: String? = null

    @SerializedName("date")
    @Expose
    lateinit var date: String

    @SerializedName("email")
    @Expose
    lateinit var email: String

    @SerializedName("num")
    @Expose
    lateinit var num: String

    @SerializedName("name")
    @Expose
    lateinit var name: String

    @SerializedName("trip")
    @Expose
    lateinit var trip: String

    @SerializedName("subject")
    @Expose
    lateinit var subject: String

    @SerializedName("op")
    @Expose
    lateinit var op: String

    @SerializedName("files")
    @Expose
    var files: MutableList<File>? = null
}