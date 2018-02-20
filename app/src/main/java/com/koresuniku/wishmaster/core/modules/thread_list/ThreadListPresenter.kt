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

import com.koresuniku.wishmaster.application.notifier.OrientationNotifier
import com.koresuniku.wishmaster.core.dagger.IWishmasterDaggerInjector
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by koresuniku on 01.01.18.
 */

class ThreadListPresenter @Inject constructor(private val injector: IWishmasterDaggerInjector,
                                              compositeDisposable: CompositeDisposable,
                                              threadListNetworkInteractor: ThreadListNetworkInteractor,
                                              threadListAdapterViewInteractor: ThreadListAdapterViewInteractor,
                                              orientationNotifier: OrientationNotifier):
        BaseThreadListPresenter(compositeDisposable, threadListNetworkInteractor, threadListAdapterViewInteractor, orientationNotifier) {
    private val LOG_TAG = ThreadListPresenter::class.java.simpleName

    override fun bindView(mvpView: ThreadListView<IThreadListPresenter>) {
        super.bindView(mvpView)
        injector.daggerThreadListPresenterComponent.inject(this)
    }

    override fun getBoardId(): String = mvpView?.getBoardId() ?: String()

    override fun loadThreadList() {
        mvpView?.showLoading()
        compositeDisposable.add(threadListNetworkInteractor.getDataFromNetwork()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    presenterData = it
                    mvpView?.onThreadListReceived(it.getBoardName())
                    threadListAdapterView?.onThreadListDataChanged(it)
                }, { it.printStackTrace(); mvpView?.showError(it.message) }))
    }

    override fun onNetworkError(t: Throwable) {
        compositeDisposable.add(Completable.fromCallable {  }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t.printStackTrace(); mvpView?.showError(t.message)}))
    }

    override fun setItemViewData(threadItemView: ThreadItemView, position: Int) {
        threadListAdapterView?.let {
            threadListAdapterViewInteractor.setItemViewData(it, threadItemView, presenterData, position)
        }
    }

    override fun getThreadListDataSize() = presenterData.getThreadList().size

    override fun getThreadItemType(position: Int): Int {
        presenterData.getThreadList()[position].files?.let {
                return when (it.size) {
                    0 -> threadListAdapterView?.NO_IMAGES_CODE ?: -1
                    1 -> threadListAdapterView?.SINGLE_IMAGE_CODE ?: -1
                    else -> threadListAdapterView?.MULTIPLE_IMAGES_CODE ?: -1
                }
            }
        return threadListAdapterView?.NO_IMAGES_CODE ?: -1
    }

    override fun onThreadItemClicked(threadNumber: String) { mvpView?.launchFullThreadActivity(threadNumber) }
}
