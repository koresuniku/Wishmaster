package com.koresuniku.wishmaster_v4.core.dashboard

import android.util.Log
import com.koresuniku.wishmaster_v4.core.data.boards.*
import com.koresuniku.wishmaster_v4.core.data.database.repository.BoardsRepository
import com.koresuniku.wishmaster_v4.core.util.search.SearchInputMatcher
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by koresuniku on 03.10.17.
 */

class DashboardPresenter @Inject constructor(private val networkInteractor: DashboardNetworkInteractor,
                                             private val databaseInteractor: DashboardDatabaseInteractor): BaseDashboardPresenter() {
    private val LOG_TAG = DashboardPresenter::class.java.simpleName

    //@Inject lateinit var boardsApiService: BoardsApiService
    //@Inject lateinit var databaseHelper: DatabaseHelper
    //@Inject lateinit var sharedPreferencesStorage: SharedPreferencesStorage

   // private lateinit var mLoadBoardObservable: Observable<BoardListData>

//    private var dashboardBoardListView: BoardListView? = null
//    private var favouriteBoardsView: FavouriteBoardsView? = null

    override fun bindView(mvpView: DashboardView) {
        super.bindView(mvpView)
        mvpView.getWishmasterApplication().getDashboardPresenterComponent().inject(this)

        //mLoadBoardObservable = getNewLoadBoardsObservable()
        //mLoadBoardObservable = mLoadBoardObservable.cache()
    }

    //fun bindDashboardBoardListView(dashboardBoardListView: BoardListView) {
//        this.dashboardBoardListView = dashboardBoardListView
//    }
//
//    fun bindFavouriteBoardsView(favouriteBoardsView: FavouriteBoardsView) {
//        this.favouriteBoardsView = favouriteBoardsView
//    }

    //fun getLoadBoardsObservable(): Observable<BoardListData> = mLoadBoardObservable

   // fun reloadBoards() { mLoadBoardObservable = getNewLoadBoardsObservable().cache() }

    override fun loadBoards() {
        Single.create(this::loadFromDatabase)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mView?.onBoardListReceived(it)
                    dashboardBoardListView?.onBoardListReceived(it)
                }, { mView?.onBoardListError(it) })
    }

    private fun loadFromDatabase(e: SingleEmitter<BoardListData>) {
        compositeDisposable.add(databaseInteractor.getDataFromDatabase()
                .subscribe({
                    if (it.getBoardList().isEmpty()) loadFromNetwork(e)
                    else { e.onSuccess(it) }
                }, { it.printStackTrace() }))
    }

    private fun loadFromNetwork(e: SingleEmitter<BoardListData>) {
        mView?.showLoading()
        compositeDisposable.add(networkInteractor.getDataFromNetwork()
                .subscribe({
                    databaseInteractor.insertAllBoardsIntoDatabase(it).subscribe()
                    e.onSuccess(it)
                }, { it.printStackTrace() }))
    }


//    private fun getNewLoadBoardsObservable(): Observable<BoardListData> {
//        return Observable.create( { e ->
//            loadBoardsFromDatabase().subscribe(
//                    { boardListData: BoardListData ->
//                        e.onNext(boardListData)
//                        mDashboardBoardListView?.onBoardListReceived(boardListData)
//                    },
//                    { throwable -> throwable.printStackTrace() },
//                    { loadBoardsFromNetwork(e) })
//        })
//    }
//
//    private fun loadBoardsFromDatabase(): Maybe<BoardListData> {
//        return Maybe.create { e -> run {
//            if (mView != null) {
//                val boardsDataFromDatabase = BoardsRepository.getBoardsDataFromDatabase(databaseHelper.readableDatabase)
//                if (boardsDataFromDatabase == null) { Log.d(LOG_TAG, "on database complete"); e.onComplete() }
//                else { Log.d(LOG_TAG, "on database success"); e.onSuccess(boardsDataFromDatabase) }
//            } else { Log.d(LOG_TAG, "on database error"); e.onError(Throwable()) }
//        }
//        }
//    }
//
//    private fun loadBoardsFromNetwork(e: ObservableEmitter<BoardListData>) {
//        mView?.showLoading()
//        val boardsObservable = boardsApiService.getBoardsObservable("get_boards")
//        compositeDisposable.add(boardsObservable
//                .subscribeOn(Schedulers.io())
//                .map(BoardsMapper::mapResponse)
//                .subscribe({ boardListData: BoardListData ->
//                    BoardsRepository.insertAllBoardsIntoDatabase(databaseHelper.writableDatabase, boardListData)
//                    e.onNext(boardListData)
//                    mDashboardBoardListView?.onBoardListReceived(boardListData)
//                }, { throwable: Throwable -> e.onError(throwable) }))
//    }

//    fun switchBoardFavourability(boardId: String) {
//        return Single.create { e -> run {
//            e.onSuccess(BoardsRepository.makeBoardFavourite(databaseHelper.writableDatabase, boardId))
//            mFavouriteBoardsView?.onFavouriteBoardListChanged(
//                    BoardsRepository.getFavouriteBoardListAsc(databaseHelper.readableDatabase))
//        }}
//    }

    fun switchBoardFavourability(boardId: String) {
        compositeDisposable.add(Single.zip(
                databaseInteractor.switchBoardFavourability(boardId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()),
                databaseInteractor.getFavouriteBoardModelListAscending()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()),
                BiFunction({ newFavouritePosition: Int, boardModelList: List<BoardModel> ->
                    dashboardBoardListView?.onFavouriteBoardPositionChanged(boardId, newFavouritePosition)
                    favouriteBoardsView?.onFavouriteBoardListChanged(boardModelList)
                    return@BiFunction
                })).subscribe())
    }

//    fun loadFavouriteBoardsList(): Single<List<BoardModel>> {
//        return Single.create { e -> run {
//            e.onSuccess(BoardsRepository.getFavouriteBoardListAsc(databaseHelper.readableDatabase))
//        }}
//    }

    fun loadFavouriteBoardList(): Single<List<BoardModel>> {
        compositeDisposable.add(databaseInteractor.getFavouriteBoardModelListAscending()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ favouriteBoardsView?.onFavouriteBoardListChanged(it) }, { it.printStackTrace() }))
    }

//    fun reorderFavouriteBoardList(boardList: List<BoardModel>): Completable {
//        return Completable.create( { e -> run {
//            BoardsRepository.reorderBoardList(databaseHelper.writableDatabase, boardList)
//            e.onComplete()
//        }})
//    }

    fun reorderFavouriteBoardList(boardList: List<BoardModel>): Completable {
        compositeDisposable.add(databaseInteractor.reorderBoardList(boardList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe())
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

    override fun shouldLaunchThreadListActivity(boardId: String) { mView?.launchThreadListActivity(boardId) }
//
//    fun unbindDashboardBoardListView() { this.dashboardBoardListView = null }
//
//    fun unbindFavouriteBoardsView() { this.favouriteBoardsView = null }

//    override fun unbindView() {
//        super.unbindView()
//        databaseHelper.readableDatabase.close()
//    }
}