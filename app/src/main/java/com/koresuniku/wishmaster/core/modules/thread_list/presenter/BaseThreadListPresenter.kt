package com.koresuniku.wishmaster.core.modules.thread_list.presenter

import com.koresuniku.wishmaster.application.listener.OrientationNotifier
import com.koresuniku.wishmaster.application.listener.OnOrientationChangedListener
import com.koresuniku.wishmaster.core.base.rx.BaseRxDataPresenter
import com.koresuniku.wishmaster.core.data.model.threads.ThreadListData
import com.koresuniku.wishmaster.core.modules.thread_list.interactor.ThreadListAdapterViewInteractor
import com.koresuniku.wishmaster.core.modules.thread_list.interactor.ThreadListNetworkInteractor
import com.koresuniku.wishmaster.core.modules.thread_list.view.ThreadListAdapterView
import com.koresuniku.wishmaster.core.modules.thread_list.view.ThreadListView
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