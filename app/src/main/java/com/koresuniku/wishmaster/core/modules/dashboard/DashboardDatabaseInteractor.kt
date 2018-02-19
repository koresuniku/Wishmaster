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

package com.koresuniku.wishmaster.core.modules.dashboard

import com.koresuniku.wishmaster.core.base.rx.BaseRxDatabaseInteractor
import com.koresuniku.wishmaster.core.data.model.boards.BoardListData
import com.koresuniku.wishmaster.core.data.model.boards.BoardListsObject
import com.koresuniku.wishmaster.core.data.model.boards.BoardModel
import com.koresuniku.wishmaster.core.data.database.DatabaseHelper
import com.koresuniku.wishmaster.core.data.database.repository.BoardsRepository
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


class DashboardDatabaseInteractor @Inject constructor(private val boardsRepository: BoardsRepository,
                                                      databaseHelper: DatabaseHelper,
                                                      compositeDisposable: CompositeDisposable):
        BaseRxDatabaseInteractor<IDashboardPresenter, BoardListData>(databaseHelper, compositeDisposable) {

    override fun getDataFromDatabase(): Single<BoardListData> {
        return Single.create({
            val boardsDataFromDatabase = boardsRepository.getBoardsDataFromDatabase(getReadableDatabase())
            it.onSuccess(boardsDataFromDatabase)
        })
    }

    fun insertAllBoardsIntoDatabase(boardListData: BoardListData): Completable {
        return Completable.create({
            boardsRepository.insertAllBoardsIntoDatabase(getWritableDatabase(), boardListData)
        })
    }

    fun switchBoardFavourability(boardId: String): Single<Int> {
        return Single.create({
            it.onSuccess(boardsRepository.switchBoardFavourability(getWritableDatabase(), boardId))
        })
    }

    fun getFavouriteBoardModelListAscending(): Single<List<BoardModel>> {
        return Single.create({
            it.onSuccess(boardsRepository.getFavouriteBoardModelListAscending(getWritableDatabase()))
        })
    }

    fun reorderBoardList(boardList: List<BoardModel>): Completable {
        return Completable.create({
            boardsRepository.reorderBoardList(getWritableDatabase(), boardList)
            it.onComplete()
        })
    }

    fun mapToBoardsDataByCategory(boardListData: BoardListData): Single<BoardListsObject> {
        return Single.create({
            it.onSuccess(boardsRepository.mapToBoardsDataByCategory(boardListData))
        })
    }
}