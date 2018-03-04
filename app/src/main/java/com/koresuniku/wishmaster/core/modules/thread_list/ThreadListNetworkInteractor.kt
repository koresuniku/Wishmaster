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

package com.koresuniku.wishmaster.core.modules.thread_list

import com.koresuniku.wishmaster.core.base.rx.BaseRxNetworkInteractor
import com.koresuniku.wishmaster.core.data.model.threads.ThreadListData
import com.koresuniku.wishmaster.core.network.thread_list_api.ThreadListResponseParser
import com.koresuniku.wishmaster.core.network.thread_list_api.ThreadListApiService
import com.koresuniku.wishmaster.core.network.thread_list_api.ThreadListJsonSchemaCatalogResponse
import com.koresuniku.wishmaster.core.network.thread_list_api.ThreadListJsonSchemaPageResponse
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class ThreadListNetworkInteractor @Inject constructor(apiService: ThreadListApiService,
                                                      private val responseParser: ThreadListResponseParser,
                                                      compositeDisposable: CompositeDisposable):
        BaseRxNetworkInteractor<IThreadListPresenter, ThreadListApiService, ThreadListData>(
                apiService, compositeDisposable) {

    override fun getDataFromNetwork(): Single<ThreadListData> {
        return Single.create({ e ->
            compositeDisposable.add(loadThreadListFromCatalog()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        if (it.threads.isEmpty()) {
                            compositeDisposable.add(loadThreadListFromPages()
                                    .subscribeOn(Schedulers.newThread())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe({
                                        e.onSuccess(responseParser.mapPageResponseToThreadListData(it))
                                    }, { presenter?.onNetworkError(it) }))
                        } else e.onSuccess(responseParser.mapCatalogResponseToThreadListData(it))
                    }, { presenter?.onNetworkError(it) }))
        })
    }

    private fun loadThreadListFromCatalog(): Single<ThreadListJsonSchemaCatalogResponse> {
        return Single.create({ e ->
            presenter?.let {
                compositeDisposable.add(getService()
                        .getThreadsObservable(it.getBoardId())
                        .subscribe(
                                { schema -> e.onSuccess(schema) },
                                { presenter?.onNetworkError(it) }))
            }
        })
    }

    private fun loadThreadListFromPages(): Single<ThreadListJsonSchemaPageResponse> {
        return Single.create({ e ->
            presenter?.let {
                val boardId = it.getBoardId()
                val indexResponse = getService().getThreadsByPageCall(boardId, "index").execute()
                indexResponse.body()?.let {
                    it.threads = arrayListOf()
                    //Абу, почини API!
                    (1 until it.pages.size - 1)
                            .map {
                                getService().getThreadsByPageCall(boardId, it.toString()).execute()
                            }
                            .forEach { nextPageResponse ->
                                it.threads.addAll(nextPageResponse.body()?.threads ?: emptyList())
                            }
                    e.onSuccess(it)
                }
            }
        })
    }
}