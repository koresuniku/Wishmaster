package com.koresuniku.wishmaster_v4.core.dashboard.interactor

import com.koresuniku.wishmaster.domain.boards_api.BoardsApiService
import com.koresuniku.wishmaster_v4.core.base.rx.BaseRxNetworkInteractorI
import com.koresuniku.wishmaster_v4.core.dashboard.presenter.IDashboardPresenterI
import com.koresuniku.wishmaster_v4.core.data.model.boards.BoardListData
import com.koresuniku.wishmaster_v4.core.network.boards_api.BoardsResponseParser
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class DashboardNetworkInteractorI @Inject constructor(
        apiService: BoardsApiService,
        private val responseParser: BoardsResponseParser,
        compositeDisposable: CompositeDisposable):
        BaseRxNetworkInteractorI<IDashboardPresenterI, BoardsApiService, BoardListData>(apiService, compositeDisposable) {


    override fun getDataFromNetwork(): Single<BoardListData> {
        return Single.create({ e -> run {
            val boardsObservable = getService().getBoardsObservable("get_boards")
            compositeDisposable.add(boardsObservable
                    .subscribeOn(Schedulers.newThread())
                    .map(responseParser::parseResponse)
                    .subscribe({ boardListData: BoardListData ->
                        e.onSuccess(boardListData)
                    }, { throwable: Throwable -> e.onError(throwable) }))
        }})

    }
}