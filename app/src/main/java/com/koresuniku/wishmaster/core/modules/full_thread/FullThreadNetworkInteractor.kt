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

package com.koresuniku.wishmaster.core.modules.full_thread

import com.koresuniku.wishmaster.core.base.rx.BaseRxNetworkInteractor
import com.koresuniku.wishmaster.core.data.model.posts.PostListData
import com.koresuniku.wishmaster.core.network.full_thread_api.FullThreadApiService
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


class FullThreadNetworkInteractor @Inject constructor(apiService: FullThreadApiService,
                                                      compositeDisposable: CompositeDisposable):
        BaseRxNetworkInteractor<
                IFullThreadPresenter,
                FullThreadApiService,
                PostListData>(apiService, compositeDisposable) {

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

    fun getPostListDataFromPosition(position: Int): Single<PostListData> {
        return Single.create({ e ->
            compositeDisposable.add(getService().getPostListObservable(
                    "get_thread",
                    presenter?.getBoardId() ?: String(),
                    presenter?.getThreadNumber() ?: String(),
                    //Абу, почини API!
                    position + 2)
                    .subscribe({
                        val data = PostListData()
                        data.postList = it
                        e.onSuccess(data)
                    }, { presenter?.onNetworkNewPostsError(it) }))
        })
    }

}