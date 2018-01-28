package com.koresuniku.wishmaster_v4.core.data.boards

import com.koresuniku.wishmaster_v4.core.data.database.repository.BoardsRepository
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