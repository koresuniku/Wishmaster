package com.koresuniku.wishmaster_v4.core.thread_list.presenter

import com.koresuniku.wishmaster_v4.core.base.rx.BaseRxDataPresenter
import com.koresuniku.wishmaster_v4.core.base.rx.BaseRxPresenter
import com.koresuniku.wishmaster_v4.core.data.model.threads.ThreadListData
import com.koresuniku.wishmaster_v4.core.thread_list.interactor.ThreadListAdapterViewInteractor
import com.koresuniku.wishmaster_v4.core.thread_list.interactor.ThreadListNetworkInteractor
import com.koresuniku.wishmaster_v4.core.thread_list.view.ThreadListAdapterView
import com.koresuniku.wishmaster_v4.core.thread_list.view.ThreadListView
import io.reactivex.disposables.CompositeDisposable

abstract class BaseThreadListPresenter(compositeDisposable: CompositeDisposable,
                                       protected val threadListNetworkInteractor: ThreadListNetworkInteractor,
                                       protected val threadListAdapterViewInteractor: ThreadListAdapterViewInteractor):
        BaseRxDataPresenter<ThreadListView<IThreadListPresenter>, ThreadListData>(compositeDisposable),
        IThreadListPresenter {
    override lateinit var presenterData: ThreadListData
    override var threadListAdapterView: ThreadListAdapterView<IThreadListPresenter>? = null

    override fun bindView(mvpView: ThreadListView<IThreadListPresenter>) {
        super.bindView(mvpView)
        presenterData = ThreadListData.emptyData()
        threadListNetworkInteractor.bindPresenter(this)
        threadListAdapterViewInteractor.bindPresenter(this)
    }

    override fun unbindView() {
        super.unbindView()
        threadListNetworkInteractor.unbindPresenter()
        threadListAdapterViewInteractor.unbindPresenter()
    }

    override fun bindThreadListAdapterView(threadListAdapterView: ThreadListAdapterView<IThreadListPresenter>) {
        this.threadListAdapterView = threadListAdapterView
    }

    override fun unbindThreadListAdapterView() {
        this.threadListAdapterView = null
    }


}