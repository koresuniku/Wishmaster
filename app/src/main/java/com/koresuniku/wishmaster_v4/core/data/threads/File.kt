package com.koresuniku.wishmaster_v4.core.data.threads

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