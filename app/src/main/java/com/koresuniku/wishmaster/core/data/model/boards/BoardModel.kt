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

package com.koresuniku.wishmaster.core.data.model.boards

import com.koresuniku.wishmaster.core.data.database.repository.BoardsRepository
import java.io.Serializable

/**
 * Created by koresuniku on 04.10.17.
 */

class BoardModel : Serializable {
    private lateinit var mBoardId: String
    private lateinit var mBoardName: String
    private lateinit var mBoardCategory: String
    private var mFavouritePosition = BoardsRepository.FAVOURITE_POSITION_DEFAULT

    fun getBoardId() = mBoardId
    fun getBoardName() = mBoardName
    fun getBoardCategory() = mBoardCategory
    fun getFavouritePosition() = mFavouritePosition

    fun setBoardId(boardId: String) { this.mBoardId = boardId }
    fun setBoardName(boardName: String) { this.mBoardName = boardName }
    fun setBoardCategory(boardCategory: String) { this.mBoardCategory = boardCategory }
    fun setFavouritePosition(favouritePosition: Int) { this.mFavouritePosition = favouritePosition }

    override fun equals(other: Any?): Boolean {
        return if (other is BoardModel) other.getBoardId() == getBoardId()
        else false
    }

    override fun toString(): String {
        return "boardId: ${getBoardId()}, " +
                "boardName: ${getBoardName()}, " +
                "boardCategory: ${getBoardCategory()}, " +
                "favouritePosition: ${getFavouritePosition()}"
    }
}