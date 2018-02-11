package com.koresuniku.wishmaster.domain.boards_api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.koresuniku.wishmaster.domain.boards_api.model.*

class BoardsJsonSchemaResponse {

    @SerializedName("Взрослым")
    @Expose
    lateinit var adults: List<Adults>
    @SerializedName("Игры")
    @Expose
    lateinit var games: List<Games>
    @SerializedName("Политика")
    @Expose
    lateinit var politics: List<Politics>
    @SerializedName("Пользовательские")
    @Expose
    lateinit var users: List<Users>
    @SerializedName("Разное")
    @Expose
    lateinit var other: List<Other>
    @SerializedName("Творчество")
    @Expose
    lateinit var creativity: List<Creativity>
    @SerializedName("Тематика")
    @Expose
    lateinit var subject: List<Subjects>
    @SerializedName("Техника и софт")
    @Expose
    lateinit var tech: List<Tech>
    @SerializedName("Японская культура")
    @Expose
    lateinit var japanese: List<Japanese>
}