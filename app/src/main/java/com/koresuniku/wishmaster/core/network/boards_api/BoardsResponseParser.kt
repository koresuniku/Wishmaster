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

package com.koresuniku.wishmaster.core.network.boards_api

import com.koresuniku.wishmaster.domain.boards_api.BoardsJsonSchemaResponse
import com.koresuniku.wishmaster.core.data.model.boards.BoardListData
import com.koresuniku.wishmaster.core.data.model.boards.BoardModel
import com.koresuniku.wishmaster.core.data.database.DatabaseContract
import javax.inject.Inject

/**
 * Created by koresuniku on 04.10.17.
 */

class BoardsResponseParser @Inject constructor() {

    fun parseResponse(boardsJsonSchemaResponse: BoardsJsonSchemaResponse): BoardListData {
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


}