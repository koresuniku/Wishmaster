package com.koresuniku.wishmaster_v4.core.data.database.repository

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.koresuniku.wishmaster_v4.core.data.boards.BoardListData
import com.koresuniku.wishmaster_v4.core.data.boards.BoardListsObject
import com.koresuniku.wishmaster_v4.core.data.boards.BoardModel
import com.koresuniku.wishmaster_v4.core.data.boards.BoardsMapper
import com.koresuniku.wishmaster_v4.core.data.database.DatabaseContract
import java.util.ArrayList
import javax.inject.Inject

/**
 * Created by koresuniku on 03.10.17.
 */

class BoardsRepository @Inject constructor() {

    private val mBoardsProjection = arrayOf(
            DatabaseContract.BoardsEntry.COLUMN_BOARD_ID,
            DatabaseContract.BoardsEntry.COLUMN_BOARD_NAME,
            DatabaseContract.BoardsEntry.COLUMN_BOARD_CATEGORY,
            DatabaseContract.BoardsEntry.COLUMN_FAVOURITE_POSITION)

    companion object {
        val CREATE_TABLE_BOARDS_STATEMENT = "CREATE TABLE " + DatabaseContract.BoardsEntry.TABLE_NAME + " (" +
                DatabaseContract.BoardsEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DatabaseContract.BoardsEntry.COLUMN_BOARD_ID + " TEXT NOT NULL, " +
                DatabaseContract.BoardsEntry.COLUMN_BOARD_NAME + " TEXT NOT NULL, " +
                DatabaseContract.BoardsEntry.COLUMN_BOARD_CATEGORY + " TEXT NOT NULL" + ");"

        const val FAVOURITE_POSITION_DEFAULT = -1

        val ALTER_TABLE_ADD_COLUMN_FAVOURITE_POSITION = "ALTER TABLE " + DatabaseContract.BoardsEntry.TABLE_NAME +
                " ADD COLUMN " + DatabaseContract.BoardsEntry.COLUMN_FAVOURITE_POSITION +
                " INTEGER DEFAULT " + FAVOURITE_POSITION_DEFAULT + ";"
    }

    fun getBoardsDataFromDatabase(database: SQLiteDatabase): BoardListData {
        val data = BoardListData()
        val boardList = ArrayList<BoardModel>()

        val cursor: Cursor = database.query(
                DatabaseContract.BoardsEntry.TABLE_NAME, mBoardsProjection,
                null, null, null, null, null)

        val columnBoardId = cursor.getColumnIndex(
                DatabaseContract.BoardsEntry.COLUMN_BOARD_ID)
        val columnBoardName = cursor.getColumnIndex(
                DatabaseContract.BoardsEntry.COLUMN_BOARD_NAME)
        val columnBoardCategory = cursor.getColumnIndex(
                DatabaseContract.BoardsEntry.COLUMN_BOARD_CATEGORY)
        val columnFavouritePosition = cursor.getColumnIndex(
                DatabaseContract.BoardsEntry.COLUMN_FAVOURITE_POSITION)

        var boardModel: BoardModel

        cursor.moveToFirst()
        if (cursor.count != 0) do {
            boardModel = BoardModel()
            boardModel.setBoardId(cursor.getString(columnBoardId))
            boardModel.setBoardName(cursor.getString(columnBoardName))
            boardModel.setBoardCategory(cursor.getString(columnBoardCategory))
            boardModel.setFavouritePosition(cursor.getInt(columnFavouritePosition))
            boardList.add(boardModel)
        } while (cursor.moveToNext())
        cursor.close()

        data.setBoardList(boardList)
        return data
    }

    fun queryBoard(database: SQLiteDatabase, boardId: String): Cursor {
        val cursor = database.query(DatabaseContract.BoardsEntry.TABLE_NAME,
                mBoardsProjection, DatabaseContract.BoardsEntry.COLUMN_BOARD_ID + " =? ",
                arrayOf(boardId), null, null, null)
        return cursor
    }

    fun insertAllBoardsIntoDatabase(database: SQLiteDatabase, data: BoardListData) {
        Log.d("BoardsRepository", "inserting all boards")
        var values: ContentValues

        data.getBoardList().forEach {
            values = ContentValues()
            values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_ID, it.getBoardId())
            values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_NAME, it.getBoardName())
            values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_CATEGORY, it.getBoardCategory())
            database.insert(DatabaseContract.BoardsEntry.TABLE_NAME, null, values)
        }
    }

    fun insertSubtractedBoardsFromInputData(database: SQLiteDatabase, inputData: BoardListData) {
        val existingBoardsData = getBoardsDataFromDatabase(database)
        val resultData = inputData.getBoardList().subtract(existingBoardsData.getBoardList())

        resultData.forEach {
            insertBoard(database, it.getBoardId(), it.getBoardName(), it.getBoardCategory())
        }
    }

    private fun insertBoard(database: SQLiteDatabase, boardId: String, boardName: String, category: String) {
        val values = ContentValues()
        values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_ID, boardId)
        values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_NAME, boardName)
        values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_CATEGORY, category)
        database.insert(DatabaseContract.BoardsEntry.TABLE_NAME, null, values)
    }

    fun deleteOldBoards(database: SQLiteDatabase, inputData: BoardListData) {
        val existingBoardsData = getBoardsDataFromDatabase(database)
        val resultData = existingBoardsData.getBoardList().subtract(inputData.getBoardList())

        resultData.forEach { deleteBoard(database, it.getBoardId()) }
    }

    private fun deleteBoard(database: SQLiteDatabase, boardId: String) {
        database.delete(DatabaseContract.BoardsEntry.TABLE_NAME,
                DatabaseContract.BoardsEntry.COLUMN_BOARD_ID + " =? ", arrayOf(boardId))
    }

    fun switchBoardFavourability(database: SQLiteDatabase, boardId: String): Int {
        val newPosition: Int
        val cursor = database.query(DatabaseContract.BoardsEntry.TABLE_NAME, mBoardsProjection,
                DatabaseContract.BoardsEntry.COLUMN_BOARD_ID + " =? ", arrayOf(boardId),
                null, null, null, null)
        cursor.moveToFirst()
        newPosition = if (cursor.getInt(cursor.getColumnIndex(DatabaseContract.BoardsEntry.COLUMN_FAVOURITE_POSITION))
                == FAVOURITE_POSITION_DEFAULT) {
            addNewFavouriteBoardToEnd(database, boardId)
        } else {
            val currentPosition = cursor.getInt(cursor.getColumnIndex(DatabaseContract.BoardsEntry.COLUMN_FAVOURITE_POSITION))
            removeFavouriteBoard(database, boardId, currentPosition)
            FAVOURITE_POSITION_DEFAULT
        }

        cursor.close()

        return newPosition
    }

    private fun addNewFavouriteBoardToEnd(database: SQLiteDatabase, boardId: String): Int {
        val cursor = database.query(DatabaseContract.BoardsEntry.TABLE_NAME, mBoardsProjection,
                DatabaseContract.BoardsEntry.COLUMN_FAVOURITE_POSITION + " !=? ",
                arrayOf(FAVOURITE_POSITION_DEFAULT.toString()),
                null, null, null, null)
        val newPosition = cursor.count
        val values = ContentValues()
        values.put(DatabaseContract.BoardsEntry.COLUMN_FAVOURITE_POSITION, newPosition)

        database.update(DatabaseContract.BoardsEntry.TABLE_NAME, values,
                DatabaseContract.BoardsEntry.COLUMN_BOARD_ID + " =? ",
                arrayOf(boardId))

        cursor.close()
        return newPosition
    }

    private fun removeFavouriteBoard(database: SQLiteDatabase, boardId: String, currentPosition: Int) {
        val cursor = database.query(DatabaseContract.BoardsEntry.TABLE_NAME, mBoardsProjection,
                DatabaseContract.BoardsEntry.COLUMN_FAVOURITE_POSITION + " !=? ",
                arrayOf(FAVOURITE_POSITION_DEFAULT.toString()),
                null, null, null)
        cursor.moveToFirst()
       if (cursor.count != 0) do {
            val position = cursor.getInt(cursor.getColumnIndex(DatabaseContract.BoardsEntry.COLUMN_FAVOURITE_POSITION))
            if (position > currentPosition) {
                val decrementedPositionValues = ContentValues()
                decrementedPositionValues.put(DatabaseContract.BoardsEntry.COLUMN_FAVOURITE_POSITION, position - 1)
                database.update(DatabaseContract.BoardsEntry.TABLE_NAME, decrementedPositionValues,
                        DatabaseContract.BoardsEntry.COLUMN_BOARD_ID + " =? ",
                        arrayOf(cursor.getString(cursor.getColumnIndex(DatabaseContract.BoardsEntry.COLUMN_BOARD_ID))))
            }
        } while (cursor.moveToNext())

        val values = ContentValues()
        values.put(DatabaseContract.BoardsEntry.COLUMN_FAVOURITE_POSITION, FAVOURITE_POSITION_DEFAULT)
        database.update(DatabaseContract.BoardsEntry.TABLE_NAME, values,
                DatabaseContract.BoardsEntry.COLUMN_BOARD_ID + " =? ", arrayOf(boardId))

        cursor.close()
    }

    fun getFavouriteBoardModelListAscending(database: SQLiteDatabase): List<BoardModel> {
        val cursor = database.query(DatabaseContract.BoardsEntry.TABLE_NAME,
                mBoardsProjection,
                DatabaseContract.BoardsEntry.COLUMN_FAVOURITE_POSITION + " !=? ",
                arrayOf(FAVOURITE_POSITION_DEFAULT.toString()),
                null,
                null,
                DatabaseContract.BoardsEntry.COLUMN_FAVOURITE_POSITION + " ASC")
               // null)
        Log.d("BR", "cursor.count: ${cursor.count}")
        val boardList = mapCursorToBoardModelList(cursor)
        cursor.close()

        return boardList
    }

    fun reorderBoardList(database: SQLiteDatabase, boardList: List<BoardModel>) {
        boardList.forEach {
            val values = ContentValues()
            values.put(DatabaseContract.BoardsEntry.COLUMN_FAVOURITE_POSITION, it.getFavouritePosition())
            database.update(
                    DatabaseContract.BoardsEntry.TABLE_NAME,
                    values,
                    DatabaseContract.BoardsEntry.COLUMN_BOARD_ID + " =? ",
                    arrayOf(it.getBoardId()))
        }
    }

    fun mapToBoardsDataByCategory(boardListData: BoardListData): BoardListsObject {
        val resultList = ArrayList<Pair<String, ArrayList<BoardModel>>>()

        var currentCategory = boardListData.getBoardList()[0].getBoardCategory()
        var currentArrayListOfNames = ArrayList<BoardModel>()
        (0 until boardListData.getBoardList().size)
                .asSequence()
                .map { boardListData.getBoardList()[it] }
                .forEach {
                    if (it.getBoardCategory() == currentCategory) {
                        currentArrayListOfNames.add(it)
                    } else {
                        resultList.add(Pair(currentCategory, currentArrayListOfNames))
                        currentArrayListOfNames = ArrayList()
                        currentArrayListOfNames.add(it)
                        currentCategory = it.getBoardCategory()
                    }
                }
        resultList.add(Pair(currentCategory, currentArrayListOfNames))

        return BoardListsObject(resultList)
    }

    private fun mapCursorToBoardModelList(cursor: Cursor): List<BoardModel> {
        val boardList = ArrayList<BoardModel>()

        cursor.moveToFirst()
        if (cursor.count != 0) do {
            val boardId = cursor.getString(cursor.getColumnIndex(DatabaseContract.BoardsEntry.COLUMN_BOARD_ID))
            val boardName = cursor.getString(cursor.getColumnIndex(DatabaseContract.BoardsEntry.COLUMN_BOARD_NAME))
            val boardCategory = cursor.getString(cursor.getColumnIndex(DatabaseContract.BoardsEntry.COLUMN_BOARD_CATEGORY))
            val favouritePosition = cursor.getInt(cursor.getColumnIndex(DatabaseContract.BoardsEntry.COLUMN_FAVOURITE_POSITION))

            val boardModel = BoardModel()
            boardModel.setBoardId(boardId)
            boardModel.setBoardName(boardName)
            boardModel.setBoardCategory(boardCategory)
            boardModel.setFavouritePosition(favouritePosition)

            boardList.add(boardModel)
        } while (cursor.moveToNext())

        return boardList
    }
}