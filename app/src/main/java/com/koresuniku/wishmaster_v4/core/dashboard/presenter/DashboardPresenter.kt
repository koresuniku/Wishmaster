package com.koresuniku.wishmaster_v4.core.dashboard.presenter

import com.koresuniku.wishmaster_v4.core.dagger.IWishmasterDaggerInjector
import com.koresuniku.wishmaster_v4.core.dashboard.interactor.DashboardDatabaseInteractor
import com.koresuniku.wishmaster_v4.core.dashboard.interactor.DashboardNetworkInteractor
import com.koresuniku.wishmaster_v4.core.dashboard.interactor.DashboardSearchInteractor
import com.koresuniku.wishmaster_v4.core.dashboard.interactor.DashboardSharedPreferencesInteractor
import com.koresuniku.wishmaster_v4.core.dashboard.view.DashboardView
import com.koresuniku.wishmaster_v4.core.data.model.boards.BoardListData
import com.koresuniku.wishmaster_v4.core.data.model.boards.BoardModel
import com.koresuniku.wishmaster_v4.core.util.search.SearchInputMatcher
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by koresuniku on 03.10.17.
 */

class DashboardPresenter @Inject constructor(private val injector: IWishmasterDaggerInjector,
                                             networkInteractor: DashboardNetworkInteractor,
                                             databaseInteractor: DashboardDatabaseInteractor,
                                             searchInteractor: DashboardSearchInteractor,
                                             sharedPreferencesInteractor: DashboardSharedPreferencesInteractor):
        BaseDashboardPresenter(networkInteractor, databaseInteractor, searchInteractor, sharedPreferencesInteractor) {
    private val LOG_TAG = DashboardPresenter::class.java.simpleName

    override fun bindView(mvpView: DashboardView<IDashboardPresenter>) {
        super.bindView(mvpView)
        injector.getDashboardPresenterComponent().inject(this)
    }

    override fun loadBoards() {
        compositeDisposable.add(Single.create(this::loadFromDatabase)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { boardList ->
                    mView?.onBoardListReceived(boardList)
                    mapToBoardsDataByCategory(boardList)
                })
    }

    private fun loadFromDatabase(e: SingleEmitter<BoardListData>) {
        compositeDisposable.add(databaseInteractor.getDataFromDatabase()
                .subscribe({
                    if (it.getBoardList().isEmpty()) loadFromNetwork(e)
                    else e.onSuccess(it)
                }, { it.printStackTrace(); mView?.onBoardListError(it) }))
    }

    private fun loadFromNetwork(e: SingleEmitter<BoardListData>) {
        mView?.showLoading()
        compositeDisposable.add(networkInteractor.getDataFromNetwork()
                .subscribe({
                    databaseInteractor.insertAllBoardsIntoDatabase(it).subscribe()
                    e.onSuccess(it)
                }, { it.printStackTrace(); mView?.onBoardListError(it) }))
    }

    override fun switchBoardFavourability(boardId: String) {
        compositeDisposable.add(databaseInteractor.switchBoardFavourability(boardId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    dashboardBoardListView?.onBoardFavourabilityChanged(boardId, it)
                    compositeDisposable.add(databaseInteractor.getFavouriteBoardModelListAscending()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    { favouriteBoardsView?.onFavouriteBoardListChanged(it) },
                                    { it.printStackTrace() }))
                }, { it.printStackTrace() }))
    }

    private fun mapToBoardsDataByCategory(boardListData: BoardListData) {
        compositeDisposable.add(databaseInteractor.mapToBoardsDataByCategory(boardListData)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { dashboardBoardListView?.onBoardListsObjectReceived(it) },
                        { it.printStackTrace() }))
    }

    override fun loadFavouriteBoardList() {
        compositeDisposable.add(databaseInteractor.getFavouriteBoardModelListAscending()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ favouriteBoardsView?.onFavouriteBoardListChanged(it) }, { it.printStackTrace() }))
    }


    override fun reorderFavouriteBoardList(boardList: List<BoardModel>) {
        compositeDisposable.add(databaseInteractor.reorderBoardList(boardList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe())
    }

    override fun processSearchInput(input: String) {
        compositeDisposable.add(searchInteractor.processInput(input)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.responseCode == SearchInputMatcher.UNKNOWN_CODE) mView?.showUnknownInput()
                    else if (it.responseCode == SearchInputMatcher.BOARD_CODE)
                        mView?.launchThreadListActivity(it.data) }, { it.printStackTrace()}))
    }

    override fun getDashboardFavouriteTabPosition() {
        compositeDisposable.add(sharedPreferencesInteractor.getDashboardFavouriteTabPosition()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ mView?.onFavouriteTabPositionReceived(it) }, { it.printStackTrace()}))
    }

    override fun shouldLaunchThreadListActivity(boardId: String) { mView?.launchThreadListActivity(boardId) }
}