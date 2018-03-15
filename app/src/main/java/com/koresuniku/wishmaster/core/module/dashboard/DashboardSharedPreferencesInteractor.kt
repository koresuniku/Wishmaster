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

package com.koresuniku.wishmaster.core.module.dashboard

import com.koresuniku.wishmaster.application.preferences.ISharedPreferencesStorage
import com.koresuniku.wishmaster.application.preferences.SharedPreferencesKeystore
import com.koresuniku.wishmaster.application.IWMDependencyInjector
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class DashboardSharedPreferencesInteractor @Inject constructor(injector: IWMDependencyInjector) :
        DashboardContract.IDashboardSharedPreferencesInteractor {

    @Inject override lateinit var sharedPreferencesStorage: ISharedPreferencesStorage
    @Inject lateinit var compositeDisposable: CompositeDisposable

    init { injector.daggerDashboardLogicComponent.inject(this) }

    override fun getDashboardFavouriteTabPosition(): Single<Int> {
        return Single.create({
            compositeDisposable.add(sharedPreferencesStorage.readInt(
                    SharedPreferencesKeystore.DASHBOARD_PREFERRED_TAB_POSITION_KEY,
                    SharedPreferencesKeystore.DASHBOARD_PREFERRED_TAB_POSITION_DEFAULT)
                    .observeOn(Schedulers.io())
                    .subscribe(it::onSuccess))
        })
    }
}