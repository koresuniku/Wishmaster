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