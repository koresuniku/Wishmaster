package com.koresuniku.wishmaster_v4.core.data.boards

/**
 * Created by koresuniku on 04.10.17.
 */

class BoardListData {
    private lateinit var boardModelList: List<BoardModel>

    fun getBoardList() = boardModelList

    fun setBoardList(boardModelList: List<BoardModel>) {
        this.boardModelList = boardModelList
    }
}