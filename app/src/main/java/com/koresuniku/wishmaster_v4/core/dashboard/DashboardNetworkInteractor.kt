package com.koresuniku.wishmaster_v4.core.dashboard

import com.koresuniku.wishmaster.domain.boards_api.BoardsApiService
import com.koresuniku.wishmaster_v4.core.base.rx.BaseRxNetworkInteractor
import com.koresuniku.wishmaster_v4.core.data.boards.BoardListData
import com.koresuniku.wishmaster_v4.core.data.boards.BoardsMapper
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class DashboardNetworkInteractor @Inject constructor(
        apiService: BoardsApiService,
        private val boardsMapper: BoardsMapper,
        compositeDisposable: CompositeDisposable):
        BaseRxNetworkInteractor<IDashboardPresenter, BoardsApiService, BoardListData>(apiService, compositeDisposable) {


    override fun getDataFromNetwork(): Single<BoardListData> {
        return Single.create({ e -> run {
            val boardsObservable = getService().getBoardsObservable("get_boards")
            compositeDisposable.add(boardsObservable
                    .subscribeOn(Schedulers.newThread())
                    .map(boardsMapper::mapResponse)
                    .subscribe({ boardListData: BoardListData ->
                        e.onSuccess(boardListData)
                    }, { throwable: Throwable -> e.onError(throwable) }))
        }})

    }
}