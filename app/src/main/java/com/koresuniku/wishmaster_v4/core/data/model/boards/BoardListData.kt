package com.koresuniku.wishmaster_v4.core.data.model.boards

/**
 * Created by koresuniku on 04.10.17.
 */

class BoardListData {
    private var boardModelList: List<BoardModel> = emptyList()

    fun getBoardList() = boardModelList

    fun setBoardList(boardModelList: List<BoardModel>) {
        this.boardModelList = boardModelList
    }
}