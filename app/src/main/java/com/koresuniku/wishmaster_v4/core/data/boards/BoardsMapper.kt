package com.koresuniku.wishmaster_v4.core.data.boards

import android.database.Cursor
import com.koresuniku.wishmaster.domain.boards_api.BoardsJsonSchemaResponse
import com.koresuniku.wishmaster_v4.core.data.database.DatabaseContract

/**
 * Created by koresuniku on 04.10.17.
 */

object BoardsMapper {

    fun mapResponse(boardsJsonSchemaResponse: BoardsJsonSchemaResponse): BoardListData {
        val boardsDataResult = BoardListData()
        val boardListResult = ArrayList<BoardModel>()
        var boardModel: BoardModel

        for (adults in boardsJsonSchemaResponse.adults) {
            boardModel = BoardModel()
            boardModel.setBoardId(adults.id)
            boardModel.setBoardName(adults.name)
            boardModel.setBoardCategory(DatabaseContract.BoardsEntry.CATEGORY_ADULTS_RUSSIAN)
            boardListResult.add(boardModel)
        }
        for (creativity in boardsJsonSchemaResponse.creativity) {
            boardModel = BoardModel()
            boardModel.setBoardId(creativity.id)
            boardModel.setBoardName(creativity.name)
            boardModel.setBoardCategory(DatabaseContract.BoardsEntry.CATEGORY_CREATIVITY_RUSSIAN)

            boardListResult.add(boardModel)
        }
        for (games in boardsJsonSchemaResponse.games) {
            boardModel = BoardModel()
            boardModel.setBoardId(games.id)
            boardModel.setBoardName(games.name)
            boardModel.setBoardCategory(DatabaseContract.BoardsEntry.CATEGORY_GAMES_RUSSIAN)
            boardListResult.add(boardModel)
        }
        for (japanese in boardsJsonSchemaResponse.japanese) {
            boardModel = BoardModel()
            boardModel.setBoardId(japanese.id)
            boardModel.setBoardName(japanese.name)
            boardModel.setBoardCategory(DatabaseContract.BoardsEntry.CATEGORY_JAPANESE_RUSSIAN)
            boardListResult.add(boardModel)
        }
        for (other in boardsJsonSchemaResponse.other) {
            boardModel = BoardModel()
            boardModel.setBoardId(other.id)
            boardModel.setBoardName(other.name)
            boardModel.setBoardCategory(DatabaseContract.BoardsEntry.CATEGORY_OTHER_RUSSIAN)
            boardListResult.add(boardModel)
        }
        for (politics in boardsJsonSchemaResponse.politics) {
            boardModel = BoardModel()
            boardModel.setBoardId(politics.id)
            boardModel.setBoardName(politics.name)
            boardModel.setBoardCategory(DatabaseContract.BoardsEntry.CATEGORY_POLITICS_RUSSIAN)
            boardListResult.add(boardModel)
        }
        for (subjects in boardsJsonSchemaResponse.subject) {
            boardModel = BoardModel()
            boardModel.setBoardId(subjects.id)
            boardModel.setBoardName(subjects.name)
            boardModel.setBoardCategory(DatabaseContract.BoardsEntry.CATEGORY_SUBJECTS_RUSSIAN)
            boardListResult.add(boardModel)
        }
        for (tech in boardsJsonSchemaResponse.tech) {
            boardModel = BoardModel()
            boardModel.setBoardId(tech.id)
            boardModel.setBoardName(tech.name)
            boardModel.setBoardCategory(DatabaseContract.BoardsEntry.CATEGORY_TECH_RUSSIAN)
            boardListResult.add(boardModel)
        }
        for (users in boardsJsonSchemaResponse.users) {
            boardModel = BoardModel()
            boardModel.setBoardId(users.id)
            boardModel.setBoardName(users.name)
            boardModel.setBoardCategory(DatabaseContract.BoardsEntry.CATEGORY_USERS_RUSSIAN)
            boardListResult.add(boardModel)
        }

        boardsDataResult.setBoardList(boardListResult)
        return boardsDataResult
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

    fun mapCursorToBoardModelList(cursor: Cursor): List<BoardModel> {
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