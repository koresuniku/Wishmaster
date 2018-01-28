package com.koresuniku.wishmaster_v4.core.dashboard

import android.util.Log
import com.koresuniku.wishmaster.domain.boards_api.BoardsApiService
import com.koresuniku.wishmaster_v4.application.SharedPreferencesStorage
import com.koresuniku.wishmaster_v4.core.base.BaseRxPresenter
import com.koresuniku.wishmaster_v4.core.data.boards.*
import com.koresuniku.wishmaster_v4.core.data.database.DatabaseHelper
import com.koresuniku.wishmaster_v4.core.data.database.repository.BoardsRepository
import com.koresuniku.wishmaster_v4.core.util.search.SearchInputMatcher
import io.reactivex.*
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by koresuniku on 03.10.17.
 */

class DashboardPresenter @Inject constructor(): BaseRxPresenter<DashboardView>() {
    private val LOG_TAG = DashboardPresenter::class.java.simpleName

    @Inject lateinit var boardsApiService: BoardsApiService
    @Inject lateinit var databaseHelper: DatabaseHelper
    @Inject lateinit var sharedPreferencesStorage: SharedPreferencesStorage

    private lateinit var mLoadBoardObservable: Observable<BoardListData>

    private var mDashboardBoardListView: BoardListView? = null
    private var mFavouriteBoardsView: FavouriteBoardsView? = null

    override fun bindView(mvpView: DashboardView) {
        super.bindView(mvpView)
        mvpView.getWishmasterApplication().getDashBoardComponent().inject(this)

        mLoadBoardObservable = getNewLoadBoardsObservable()
        mLoadBoardObservable = mLoadBoardObservable.cache()
    }

    fun bindDashboardBoardListView(dashboardBoardListView: BoardListView) {
        this.mDashboardBoardListView = dashboardBoardListView
    }

    fun bindFavouriteBoardsView(favouriteBoardsView: FavouriteBoardsView) {
        this.mFavouriteBoardsView = favouriteBoardsView
    }

    fun getLoadBoardsObservable(): Observable<BoardListData> = mLoadBoardObservable

    fun reloadBoards() { mLoadBoardObservable = getNewLoadBoardsObservable().cache() }

    private fun getNewLoadBoardsObservable(): Observable<BoardListData> {
        return Observable.create( { e ->
            loadBoardsFromDatabase().subscribe(
                    { boardListData: BoardListData ->
                        e.onNext(boardListData)
                        mDashboardBoardListView?.onBoardsDataReceived(boardListData)
                    },
                    { throwable -> throwable.printStackTrace() },
                    { loadBoardsFromNetwork(e) })
        })
    }

    private fun loadBoardsFromDatabase(): Maybe<BoardListData> {
        return Maybe.create { e -> run {
            if (mView != null) {
                val boardsDataFromDatabase = BoardsRepository.getBoardsDataFromDatabase(databaseHelper.readableDatabase)
                if (boardsDataFromDatabase == null) { Log.d(LOG_TAG, "on database complete"); e.onComplete() }
                else { Log.d(LOG_TAG, "on database success"); e.onSuccess(boardsDataFromDatabase) }
            } else { Log.d(LOG_TAG, "on database error"); e.onError(Throwable()) }
        }
        }
    }

    private fun loadBoardsFromNetwork(e: ObservableEmitter<BoardListData>) {
        mView?.showLoading()
        val boardsObservable = boardsApiService.getBoardsObservable("get_boards")
        compositeDisposable.add(boardsObservable
                .subscribeOn(Schedulers.io())
                .map(BoardsMapper::mapResponse)
                .subscribe({ boardListData: BoardListData ->
                    BoardsRepository.insertAllBoardsIntoDatabase(databaseHelper.writableDatabase, boardListData)
                    e.onNext(boardListData)
                    mDashboardBoardListView?.onBoardsDataReceived(boardListData)
                }, { throwable: Throwable -> e.onError(throwable) }))
    }

    fun makeBoardFavourite(boardId: String): Single<Int> {
        return Single.create { e -> run {
            e.onSuccess(BoardsRepository.makeBoardFavourite(databaseHelper.writableDatabase, boardId))
            mFavouriteBoardsView?.onFavouriteBoardListChanged(
                    BoardsRepository.getFavouriteBoardListAsc(databaseHelper.readableDatabase))
        }}
    }

    fun loadFavouriteBoardsList(): Single<List<BoardModel>> {
        return Single.create { e -> run {
            e.onSuccess(BoardsRepository.getFavouriteBoardListAsc(databaseHelper.readableDatabase))
        }}
    }

    fun reorderFavouriteBoardList(boardList: List<BoardModel>): Completable {
        return Completable.create( { e -> run {
            BoardsRepository.reorderBoardList(databaseHelper.writableDatabase, boardList)
            e.onComplete()
        }})
    }

    fun processSearchInput(input: String) {
        val response = SearchInputMatcher.matchInput(input)
        if (response.responseCode == SearchInputMatcher.UNKNOWN_CODE) {
            Log.d(LOG_TAG, "unknown")
            mView?.showUnknownInput()
        } else if (response.responseCode == SearchInputMatcher.BOARD_CODE) {
            mView?.launchThreadListActivity(response.data)
        }
    }

    fun shouldLaunchThreadListActivity(boardId: String) { mView?.launchThreadListActivity(boardId) }

    fun unbindDashboardBoardListView() { this.mDashboardBoardListView = null }

    fun unbindFavouriteBoardsView() { this.mFavouriteBoardsView = null }

    override fun unbindView() {
        super.unbindView()
        databaseHelper.readableDatabase.close()
    }
}