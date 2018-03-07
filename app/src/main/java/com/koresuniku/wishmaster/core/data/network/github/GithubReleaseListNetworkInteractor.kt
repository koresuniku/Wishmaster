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

package com.koresuniku.wishmaster.core.data.network.github

import com.koresuniku.wishmaster.core.base.INetworkInteractor
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/**
 * Created by koresuniku on 2/18/18.
 */

class GithubReleaseListNetworkInteractor @Inject constructor(private val apiService: GithubApiService,
                                                             private val compositeDisposable: CompositeDisposable):
        INetworkInteractor<GithubApiService, List<Release>> {

    override fun getService() = apiService

    override fun getDataFromNetwork(): Single<List<Release>> {
        return Single.create({ e ->
            compositeDisposable.add(getService()
                    .getRealeaseList()
                    .subscribe({ e.onSuccess(it) }, { it.printStackTrace() }))
        })
    }
}