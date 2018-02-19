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

package com.koresuniku.wishmaster.core.data.model.threads

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