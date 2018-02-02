package com.koresuniku.wishmaster_v4.core.thread_list.presenter

import com.koresuniku.wishmaster_v4.core.base.rx.BaseRxDataPresenter
import com.koresuniku.wishmaster_v4.core.base.rx.BaseRxPresenter
import com.koresuniku.wishmaster_v4.core.data.model.threads.ThreadListData
import com.koresuniku.wishmaster_v4.core.thread_list.interactor.ThreadListNetworkInteractor
import com.koresuniku.wishmaster_v4.core.thread_list.view.ThreadListAdapterView
import com.koresuniku.wishmaster_v4.core.thread_list.view.ThreadListView
import io.reactivex.disposables.CompositeDisposable

abstract class BaseThreadListPresenter(compositeDisposable: CompositeDisposable,
                                       protected val threadListNetworkInteractor: ThreadListNetworkInteractor):
        BaseRxDataPresenter<ThreadListView<IThreadListPresenter>, ThreadListData>(compositeDisposable),
        IThreadListPresenter {
    override var presenterData: ThreadListData = ThreadListData.emptyData()
    override var threadListAdapterView: ThreadListAdapterView<IThreadListPresenter>? = null

    override fun bindView(mvpView: ThreadListView<IThreadListPresenter>) {
        super.bindView(mvpView)
        threadListNetworkInteractor.bindPresenter(this)
    }

    override fun unbindView() {
        super.unbindView()
        threadListNetworkInteractor.unbindPresenter()
    }

    override fun bindThreadListAdapterView(threadListAdapterView: ThreadListAdapterView<IThreadListPresenter>) {
        this.threadListAdapterView = threadListAdapterView
    }

    override fun unbindThreadListAdapterView() {
        this.threadListAdapterView = null
    }


}