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

import com.koresuniku.wishmaster.application.IWMDependencyInjector
import com.koresuniku.wishmaster.domain.boards_api.BoardsApiService
import com.koresuniku.wishmaster.core.data.model.boards.BoardListData
import com.koresuniku.wishmaster.core.data.network.boards_api.BoardsResponseParser
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class DashboardNetworkInteractor @Inject constructor(injector: IWMDependencyInjector):
        DashboardContract.IDashboardNetworkInteractor {

    @Inject override lateinit var service: BoardsApiService
    @Inject lateinit var compositeDisposable: CompositeDisposable
    @Inject lateinit var responseParser: BoardsResponseParser

    init { injector.daggerDashboardLogicComponent.inject(this) }

    override fun fetchBoardListData(): Single<BoardListData> {
        return Single.create({ e -> run {
            val boardsObservable = service.getBoardsObservable("get_boards")
            compositeDisposable.add(boardsObservable
                    .subscribeOn(Schedulers.newThread())
                    .map(responseParser::parseResponse)
                    .subscribe({ boardListData: BoardListData ->
                        e.onSuccess(boardListData)
                    }, { e.onError(it) }))
        }})
    }


}