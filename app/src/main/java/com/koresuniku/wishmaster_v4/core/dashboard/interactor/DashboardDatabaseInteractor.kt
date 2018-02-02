package com.koresuniku.wishmaster_v4.core.dashboard.interactor

import com.koresuniku.wishmaster_v4.core.base.rx.BaseRxDatabaseInteractorI
import com.koresuniku.wishmaster_v4.core.dashboard.presenter.IDashboardPresenter
import com.koresuniku.wishmaster_v4.core.data.model.boards.BoardListData
import com.koresuniku.wishmaster_v4.core.data.model.boards.BoardListsObject
import com.koresuniku.wishmaster_v4.core.data.model.boards.BoardModel
import com.koresuniku.wishmaster_v4.core.data.database.DatabaseHelper
import com.koresuniku.wishmaster_v4.core.data.database.repository.BoardsRepository
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


class DashboardDatabaseInteractor @Inject constructor(private val boardsRepository: BoardsRepository,
                                                      databaseHelper: DatabaseHelper,
                                                      compositeDisposable: CompositeDisposable):
        BaseRxDatabaseInteractorI<IDashboardPresenter, BoardListData>(databaseHelper, compositeDisposable) {

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