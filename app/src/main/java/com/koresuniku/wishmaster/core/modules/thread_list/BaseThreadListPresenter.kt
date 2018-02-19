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

import com.koresuniku.wishmaster.application.listener.OrientationNotifier
import com.koresuniku.wishmaster.application.listener.OnOrientationChangedListener
import com.koresuniku.wishmaster.core.base.rx.BaseRxDataPresenter
import com.koresuniku.wishmaster.core.data.model.threads.ThreadListData
import io.reactivex.disposables.CompositeDisposable

abstract class BaseThreadListPresenter(compositeDisposable: CompositeDisposable,
                                       protected val threadListNetworkInteractor: ThreadListNetworkInteractor,
                                       protected val threadListAdapterViewInteractor: ThreadListAdapterViewInteractor,
                                       private val orientationNotifier: OrientationNotifier):
        BaseRxDataPresenter<ThreadListView<IThreadListPresenter>, ThreadListData>(compositeDisposable),
        IThreadListPresenter, OnOrientationChangedListener {
    override lateinit var presenterData: ThreadListData
    override var threadListAdapterView: ThreadListAdapterView<IThreadListPresenter>? = null

    override fun onOrientationChanged(orientation: Int) {
        threadListAdapterView?.onOrientationChanged(orientation)
    }

    override fun isDataLoaded() = presenterData.getThreadList().size != 0
    override fun getDataSize() = presenterData.getThreadList().size

    override fun bindView(mvpView: ThreadListView<IThreadListPresenter>) {
        super.bindView(mvpView)
        presenterData = ThreadListData.emptyData()
        threadListNetworkInteractor.bindPresenter(this)
        threadListAdapterViewInteractor.bindPresenter(this)
        orientationNotifier.bindListener(this)
    }

    override fun unbindView() {
        super.unbindView()
        threadListNetworkInteractor.unbindPresenter()
        threadListAdapterViewInteractor.unbindPresenter()
        unbindThreadListAdapterView()
        orientationNotifier.unbindListener(this)
    }

    override fun bindThreadListAdapterView(threadListAdapterView: ThreadListAdapterView<IThreadListPresenter>) {
        this.threadListAdapterView = threadListAdapterView
    }

    override fun unbindThreadListAdapterView() {
        this.threadListAdapterView = null
    }
}