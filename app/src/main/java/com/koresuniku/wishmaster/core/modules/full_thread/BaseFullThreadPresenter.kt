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

import com.koresuniku.wishmaster.application.notifier.OnOrientationChangedListener
import com.koresuniku.wishmaster.application.notifier.OrientationNotifier
import com.koresuniku.wishmaster.core.data.model.posts.PostListData
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by koresuniku on 2/11/18.
 */

abstract class BaseFullThreadPresenter(compositeDisposable: CompositeDisposable,
                                       protected val networkInteractor: FullThreadNetworkInteractor,
                                       protected val adapterViewInteractor: FullThreadAdapterViewInteractor,
                                       private val orientationNotifier: OrientationNotifier) :
        BaseRxDataPresenter<FullThreadView<IFullThreadPresenter>, PostListData>(compositeDisposable),
        IFullThreadPresenter, OnOrientationChangedListener {
    override var fullThreadAdapterView: FullThreadAdapterView<IFullThreadPresenter>? = null
    override lateinit var presenterData: PostListData
    override fun isDataLoaded() = presenterData.postList.size != 0
    override fun getDataSize() = presenterData.postList.size

    override fun bindView(mvpView: FullThreadView<IFullThreadPresenter>) {
        super.bindView(mvpView)
        presenterData = PostListData.emptyData()
        networkInteractor.bindPresenter(this)
        adapterViewInteractor.bindPresenter(this)
        orientationNotifier.bindListener(this)
    }

    override fun onOrientationChanged(orientation: Int) {
        fullThreadAdapterView?.onOrientationChanged(orientation)
    }

    override fun unbindView() {
        super.unbindView()
        networkInteractor.unbindPresenter()
        adapterViewInteractor.unbindPresenter()
        orientationNotifier.unbindListener(this)
    }

    override fun bindFullThreadAdapterView(fullThreadAdapterView: FullThreadAdapterView<IFullThreadPresenter>) {
        this.fullThreadAdapterView = fullThreadAdapterView
    }

    override fun unbindFullThreadAdapterView() {
        this.fullThreadAdapterView = null
    }
}