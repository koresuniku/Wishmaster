package com.koresuniku.wishmaster_v4.core.data.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.koresuniku.wishmaster_v4.core.data.database.repository.BoardsRepository
import javax.inject.Inject

/**
 * Created by koresuniku on 10.11.17.
 */

class DatabaseHelper @Inject constructor(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private val DATABASE_NAME = "wishmaster.db"
        private val DATABASE_VERSION = 2
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