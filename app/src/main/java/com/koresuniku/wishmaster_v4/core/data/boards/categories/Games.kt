package com.koresuniku.wishmaster.domain.boards_api.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Games {

    @SerializedName("id")
    @Expose
    lateinit var id: String
    @SerializedName("name")
    @Expose
    lateinit var name: String

}
