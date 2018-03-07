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

import io.reactivex.Maybe
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import android.util.Log
import com.koresuniku.wishmaster.BuildConfig


/**
 * Created by koresuniku on 2/18/18.
 */

class GithubHelper @Inject constructor(private val networkInteractor: GithubReleaseListNetworkInteractor,
                                       private val compositeDisposable: CompositeDisposable) {

    companion object {
        const val GITHUB_BASE_URL = "https://api.github.com"
    }

    fun checkForNewRelease(): Maybe<Asset> {
        return Maybe.create { e ->
            compositeDisposable.add(
                    networkInteractor
                            .fetchReleaseList()
                            .subscribe({
                                Log.d("GH", "releases count: ${it.size}")
                                when {
                                    it.isEmpty() -> e.onComplete()
                                    compareVersionNames(BuildConfig.VERSION_NAME, it[0].tagName) ->
                                        e.onSuccess(it[0].assetList[0])
                                    else -> e.onComplete()
                                }
                            }, { it.printStackTrace()}))
        }
    }

    private fun compareVersionNames(current: String, latest: String): Boolean {
        val c = current.replace(Regex("[^0-9]+"), "")
        val l = latest.replace(Regex("[^0-9]+"), "")
        l.zip(c).forEach { if (it.first.toInt() > it.second.toInt()) return true }
        return false
    }
}