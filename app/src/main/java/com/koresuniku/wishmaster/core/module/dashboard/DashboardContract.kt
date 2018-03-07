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

package com.koresuniku.wishmaster.core.module.dashboard

import com.koresuniku.wishmaster.core.base.IDatabaseInteractor
import com.koresuniku.wishmaster.core.base.INetworkInteractor
import com.koresuniku.wishmaster.core.base.ISharedPreferencesInteractor
import com.koresuniku.wishmaster.core.base.IMvpInteractor
import com.koresuniku.wishmaster.core.base.IMvpPresenter
import com.koresuniku.wishmaster.core.base.IMvpView
import com.koresuniku.wishmaster.core.data.model.boards.BoardListData
import com.koresuniku.wishmaster.core.data.model.boards.BoardListsObject
import com.koresuniku.wishmaster.core.data.model.boards.BoardModel
import com.koresuniku.wishmaster.domain.boards_api.BoardsApiService
import io.reactivex.Completable
import io.reactivex.Single

/**
* Created by koresuniku on 3/5/18.
*/

object DashboardContract {

    //View
    interface IDashboardMainView : IMvpView {
        fun showLoading()
        fun onBoardListReceived(boardListData: BoardListData)
        fun onBoardListError(t: Throwable)
        fun onFavouriteTabPositionReceived(position: Int)
        fun launchThreadListActivity(boardId: String)
        fun showUnknownInput()
    }

    interface IDashboardFavouriteBoardsView : IMvpView {
        fun onFavouriteBoardListChanged(boardList: List<BoardModel>)
    }

    interface IDashboardBoardListView : IMvpView {
        fun onBoardListsObjectReceived(boardListsObject: BoardListsObject)
        fun onBoardFavourabilityChanged(boardId: String, newFavouritePosition: Int)
    }

    interface IDashboardFavouriteThreadsView : IMvpView {}

    interface IDashboardHistoryView : IMvpView {}


    //Presenter
    interface IDashboardPresenter : IMvpPresenter<IDashboardMainView> {
        var dashboardBoardListView: IDashboardBoardListView?
        var favouriteBoardsView: IDashboardFavouriteBoardsView?

        fun bindDashboardBoardListView(dashboardBoardListView: IDashboardBoardListView)
        fun bindFavouriteBoardsView(favouriteBoardsView: IDashboardFavouriteBoardsView)

        fun loadBoards()
        fun launchThreadList(boardId: String)
        fun processSearchInput(input: String)
        fun reorderFavouriteBoardList(boardList: List<BoardModel>)
        fun loadFavouriteBoardList()
        fun switchBoardFavourability(boardId: String)
        fun getDashboardFavouriteTabPosition()
        fun onNetworkError(t: Throwable)

        fun unbindDashboardBoardListView()
        fun unbindFavouriteBoardsView()
    }


    //Interactor
    interface IDashboardNetworkInteractor : INetworkInteractor<BoardsApiService> {
        fun fetchBoardListData(): Single<BoardListData>
    }

    interface IDashboardDatabaseInteractor : IDatabaseInteractor {
        fun fetchBoardListData(): Single<BoardListData>
        fun insertBoardsIntoDatabase(boardListData: BoardListData): Completable
        fun switchBoardFavourability(boardId: String): Single<Int>
        fun getFavouriteBoardModelListAscending(): Single<List<BoardModel>>
        fun reorderBoardList(boardList: List<BoardModel>): Completable
        fun mapToBoardsDataByCategory(boardListData: BoardListData): Single<BoardListsObject>
    }

    interface IDashboardSharedPreferencesInteractor : ISharedPreferencesInteractor {
        fun getDashboardFavouriteTabPosition(): Single<Int>
    }

    interface IDashboardSearchInteractor : IMvpInteractor {
        val matcher: SearchInputMatcher
        fun processInput(input: String): Single<SearchInputResponse>
    }
}