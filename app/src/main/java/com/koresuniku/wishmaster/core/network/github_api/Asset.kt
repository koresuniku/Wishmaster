package com.koresuniku.wishmaster.core.network.github_api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by koresuniku on 2/18/18.
 */

class Asset {

    @SerializedName("browser_download_url")
    @Expose
    lateinit var downloadLink: String

    @SerializedName("name")
    @Expose
    lateinit var name: String

}