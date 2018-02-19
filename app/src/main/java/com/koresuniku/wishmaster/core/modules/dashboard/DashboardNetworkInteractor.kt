package com.koresuniku.wishmaster.core.modules.dashboard

import com.koresuniku.wishmaster.domain.boards_api.BoardsApiService
import com.koresuniku.wishmaster.core.base.rx.BaseRxNetworkInteractor
import com.koresuniku.wishmaster.core.data.model.boards.BoardListData
import com.koresuniku.wishmaster.core.network.boards_api.BoardsResponseParser
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class DashboardNetworkInteractor @Inject constructor(
        apiService: BoardsApiService,
        private val responseParser: BoardsResponseParser,
        compositeDisposable: CompositeDisposable):
        BaseRxNetworkInteractor<IDashboardPresenter, BoardsApiService, BoardListData>(apiService, compositeDisposable) {


    override fun getDataFromNetwork(): Single<BoardListData> {
        return Single.create({ e -> run {
            val boardsObservable = getService().getBoardsObservable("get_boards")
            compositeDisposable.add(boardsObservable
                    .subscribeOn(Schedulers.newThread())
                    .map(responseParser::parseResponse)
                    .subscribe({ boardListData: BoardListData ->
                        e.onSuccess(boardListData)
                    }, { presenter?.onNetworkError(it) }))
        }})

    }
}