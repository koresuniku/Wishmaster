package com.koresuniku.wishmaster.core.network.github_api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by koresuniku on 2/18/18.
 */

class WishmasterRelease {

    @SerializedName("tag_name")
    @Expose
    lateinit var tagName: String
}