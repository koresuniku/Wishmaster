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

package com.koresuniku.wishmaster.core.data.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.koresuniku.wishmaster.core.data.database.repository.BoardsRepository
import javax.inject.Inject

/**
 * Created by koresuniku on 10.11.17.
 */

class DatabaseHelper @Inject constructor(context: Context) :
        SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "wishmaster.db"
        private const val DATABASE_VERSION = 2
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(BoardsRepository.CREATE_TABLE_BOARDS_STATEMENT)
        db.execSQL(BoardsRepository.ALTER_TABLE_ADD_COLUMN_FAVOURITE_POSITION)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        when (oldVersion) {
            1 -> db?.execSQL(BoardsRepository.ALTER_TABLE_ADD_COLUMN_FAVOURITE_POSITION)
        }
    }
}