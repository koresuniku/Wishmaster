package com.koresuniku.wishmaster.core.modules.full_thread.interactor

import com.koresuniku.wishmaster.core.base.rx.BaseRxNetworkInteractor
import com.koresuniku.wishmaster.core.data.model.posts.PostListData
import com.koresuniku.wishmaster.core.modules.full_thread.presenter.IFullThreadPresenter
import com.koresuniku.wishmaster.core.network.full_thread_api.FullThreadApiService
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


class FullThreadNetworkInteractor @Inject constructor(apiService: FullThreadApiService,
                                                      compositeDisposable: CompositeDisposable):
        BaseRxNetworkInteractor<IFullThreadPresenter, FullThreadApiService, PostListData>(apiService, compositeDisposable)  {

    override fun getDataFromNetwork(): Single<PostListData> {
        return Single.create({ e ->
            compositeDisposable.add(getService().getPostListObservable(
                    "get_thread",
                    presenter?.getBoardId() ?: String(),
                    presenter?.getThreadNumber() ?: String(),
                    0)
                    .subscribe({
                        val data = PostListData()
                        data.postList = it
                        e.onSuccess(data)
                    }, { presenter?.onNetworkError(it) }))
        })
    }
}