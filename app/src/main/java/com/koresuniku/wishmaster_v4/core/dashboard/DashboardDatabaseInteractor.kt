package com.koresuniku.wishmaster_v4.core.dashboard

import com.koresuniku.wishmaster_v4.core.base.rx.BaseRxDatabaseInteractor
import com.koresuniku.wishmaster_v4.core.data.boards.BoardListData
import com.koresuniku.wishmaster_v4.core.data.boards.BoardModel
import com.koresuniku.wishmaster_v4.core.data.database.DatabaseHelper
import com.koresuniku.wishmaster_v4.core.data.database.repository.BoardsRepository
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable


class DashboardDatabaseInteractor(private val boardsRepository: BoardsRepository,
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
}