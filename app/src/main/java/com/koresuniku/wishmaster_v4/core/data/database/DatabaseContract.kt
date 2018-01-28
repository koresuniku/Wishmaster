package com.koresuniku.wishmaster_v4.core.data.database

import android.net.Uri
import android.provider.BaseColumns

/**
 * Created by koresuniku on 03.10.17.
 */

object DatabaseContract {

    val CONTENT_AUTHORITY = "com.koresuniku.wishmaster"
    val BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY)!!

    object BoardsEntry : BaseColumns {
        val TABLE_NAME = "boards"

        val COLUMN_ID = "_id"
        val COLUMN_BOARD_ID = "board_id"
        val COLUMN_BOARD_NAME = "board_name"
        val COLUMN_BOARD_CATEGORY = "board_category"
        val COLUMN_FAVOURITE_POSITION = "favourite_position"

        val CATEGORY_ADULTS: String = "adults"
        val CATEGORY_CREATIVITY: String = "creativity"
        val CATEGORY_GAMES: String = "games"
        val CATEGORY_JAPANESE: String = "japanese"
        val CATEGORY_OTHER: String = "other"
        val CATEGORY_POLITICS: String = "politics"
        val CATEGORY_SUBJECTS: String = "subjects"
        val CATEGORY_TECH: String = "tech"
        val CATEGORY_USERS: String = "users"

        val CATEGORY_ADULTS_RUSSIAN: String = "Взрослым"
        val CATEGORY_CREATIVITY_RUSSIAN: String = "Творчество"
        val CATEGORY_GAMES_RUSSIAN: String = "Игры"
        val CATEGORY_JAPANESE_RUSSIAN: String = "Японская культура"
        val CATEGORY_OTHER_RUSSIAN: String = "Разное"
        val CATEGORY_POLITICS_RUSSIAN: String = "Политика"
        val CATEGORY_SUBJECTS_RUSSIAN: String = "Тематика"
        val CATEGORY_TECH_RUSSIAN: String = "Техника и софт"
        val CATEGORY_USERS_RUSSIAN: String = "Пользовательские"
    }
}