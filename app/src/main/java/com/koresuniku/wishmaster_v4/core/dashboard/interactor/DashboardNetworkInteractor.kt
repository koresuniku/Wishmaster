package com.koresuniku.wishmaster_v4.core.dashboard.interactor

import com.koresuniku.wishmaster.domain.boards_api.BoardsApiService
import com.koresuniku.wishmaster_v4.core.base.rx.BaseRxNetworkInteractor
import com.koresuniku.wishmaster_v4.core.dashboard.presenter.IDashboardPresenter
import com.koresuniku.wishmaster_v4.core.data.model.boards.BoardListData
import com.koresuniku.wishmaster_v4.core.network.boards_api.BoardsResponseParser
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
                    }, { it.printStackTrace() }))
        }})

    }
}