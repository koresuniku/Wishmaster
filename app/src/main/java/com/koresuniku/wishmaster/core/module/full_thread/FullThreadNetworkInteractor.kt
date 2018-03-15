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

package com.koresuniku.wishmaster.core.module.full_thread

import com.koresuniku.wishmaster.application.IWMDependencyInjector
import com.koresuniku.wishmaster.core.data.model.posts.PostListData
import com.koresuniku.wishmaster.core.data.network.full_thread_api.FullThreadApiService
import com.koresuniku.wishmaster.core.data.network.full_thread_api.FullThreadResponseParser
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


class FullThreadNetworkInteractor @Inject constructor(injector: IWMDependencyInjector):
        FullThreadContract.IFullThreadNetworkInteractor {

    @Inject lateinit var compositeDisposable: CompositeDisposable
    @Inject override lateinit var service: FullThreadApiService
    @Inject lateinit var responseParser: FullThreadResponseParser

    init { injector.daggerFullThreadLogicComponent.inject(this) }

    override fun fetchPostListData(boardId: String, threadNumber: String): Single<PostListData> {
        return Single.create({ e ->
            compositeDisposable.add(service.getPostListObservable(
                    "get_thread", boardId, threadNumber, 0)
                    .subscribe({
                        val data = PostListData()
                        data.postList = it
                        data.fileList = responseParser.obtainFileList(data)
                        e.onSuccess(data)
                    }, { it.printStackTrace() }))
        })
    }

    override fun fetchPostListDataStartingAt(position: Int, boardId: String, threadNumber: String):
            Single<PostListData> {
        return Single.create({ e ->
            compositeDisposable.add(service.getPostListObservable(
                    "get_thread", boardId, threadNumber, position + 2)
                    .subscribe({
                        val data = PostListData()
                        data.postList = it
                        data.fileList = responseParser.obtainFileList(data)
                        e.onSuccess(data)
                    }, { it.printStackTrace() }))
        })
    }

}