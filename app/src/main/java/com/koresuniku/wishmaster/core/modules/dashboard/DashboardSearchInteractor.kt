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

package com.koresuniku.wishmaster.core.modules.dashboard

import com.koresuniku.wishmaster.core.dagger.IWishmasterDaggerInjector
import com.koresuniku.wishmaster.core.utils.search.SearchInputMatcher
import com.koresuniku.wishmaster.core.utils.search.SearchInputResponse
import io.reactivex.Single
import javax.inject.Inject


class DashboardSearchInteractor @Inject constructor(injector: IWishmasterDaggerInjector):
        DashboardMvpContract.IDashboardSearchInteractor {

    @Inject override lateinit var matcher: SearchInputMatcher

    init { injector.daggerDashboardBusinessLogicComponent.inject(this) }

    override fun processInput(input: String): Single<SearchInputResponse> {
        return Single.create({
            val response = matcher.matchInput(input)
            it.onSuccess(response)
        })
    }
}