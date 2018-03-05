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

import android.database.sqlite.SQLiteDatabase
import com.koresuniku.wishmaster.core.base.BaseDatabaseInteractor
import com.koresuniku.wishmaster.core.dagger.IWishmasterDaggerInjector
import com.koresuniku.wishmaster.core.data.model.boards.BoardListData
import com.koresuniku.wishmaster.core.data.model.boards.BoardListsObject
import com.koresuniku.wishmaster.core.data.model.boards.BoardModel
import com.koresuniku.wishmaster.core.data.database.DatabaseHelper
import com.koresuniku.wishmaster.core.data.database.repository.BoardsRepository
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


class DashboardDatabaseInteractor @Inject constructor(injector: IWishmasterDaggerInjector):
        BaseDatabaseInteractor(), DashboardMvpContract.IDashboardDatabaseInteractor {

    @Inject override lateinit var databaseHelper: DatabaseHelper
    @Inject lateinit var compositeDisposable: CompositeDisposable
    @Inject lateinit var boardsRepository: BoardsRepository

    init { injector.daggerDashboardBusinessLogicComponent.inject(this) }

    override fun fetchBoardListData(): Single<BoardListData> {
        return Single.create({
            val boardsDataFromDatabase = boardsRepository.getBoardsDataFromDatabase(readableDatabase())
            it.onSuccess(boardsDataFromDatabase)
        })
    }

    override fun insertBoardsIntoDatabase(boardListData: BoardListData): Completable {
        return Completable.create({
            boardsRepository.insertAllBoardsIntoDatabase(writableDatabase(), boardListData)
        })
    }

    override fun switchBoardFavourability(boardId: String): Single<Int> {
        return Single.create({
            it.onSuccess(boardsRepository.switchBoardFavourability(writableDatabase(), boardId))
        })
    }

    override fun getFavouriteBoardModelListAscending(): Single<List<BoardModel>> {
        return Single.create({
            it.onSuccess(boardsRepository.getFavouriteBoardModelListAscending(writableDatabase()))
        })
    }

    override fun reorderBoardList(boardList: List<BoardModel>): Completable {
        return Completable.create({
            boardsRepository.reorderBoardList(writableDatabase(), boardList)
            it.onComplete()
        })
    }

    override fun mapToBoardsDataByCategory(boardListData: BoardListData): Single<BoardListsObject> {
        return Single.create({
            it.onSuccess(boardsRepository.mapToBoardsDataByCategory(boardListData))
        })
    }


}