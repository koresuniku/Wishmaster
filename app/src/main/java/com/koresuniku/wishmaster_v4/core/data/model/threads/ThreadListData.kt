package com.koresuniku.wishmaster_v4.core.data.model.threads

/**
 * Created by koresuniku on 01.01.18.
 */

class ThreadListData {
    private lateinit var boardId: String
    private lateinit var boardName: String
    private lateinit var defaultName: String
    private lateinit var threadList: MutableList<Thread>
    private var pagesCount: Int = 0

    fun getBoardId() = boardId
    fun getThreadList() = threadList
    fun getBoardName() = boardName
    fun getDefaultName() = defaultName
    fun getPagesCount() = pagesCount

    fun setBoardId(boardId: String) { this.boardId = boardId }
    fun setBoardList(threadList: MutableList<Thread>) { this.threadList = threadList }
    fun setBoardName(boardName: String) { this.boardName = boardName }
    fun setDefaultName(defaultName: String) { this.defaultName = defaultName }
    fun setPagesCount(pagesCount: Int) { this.pagesCount = pagesCount }

    companion object {

        fun emptyData(): ThreadListData {
            val data = ThreadListData()

            data.setBoardId(String())
            data.setBoardName(String())
            data.setDefaultName(String())
            data.setPagesCount(0)
            data.setBoardList(arrayListOf())

            return data
        }
    }
}