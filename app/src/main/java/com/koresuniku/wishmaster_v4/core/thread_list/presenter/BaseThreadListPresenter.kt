package com.koresuniku.wishmaster_v4.core.thread_list.presenter

import android.util.Log
import com.koresuniku.wishmaster_v4.application.listener.OrientationNotifier
import com.koresuniku.wishmaster_v4.application.listener.OnOrientationChangedListener
import com.koresuniku.wishmaster_v4.core.base.rx.BaseRxDataPresenter
import com.koresuniku.wishmaster_v4.core.data.model.threads.ThreadListData
import com.koresuniku.wishmaster_v4.core.thread_list.interactor.ThreadListAdapterViewInteractor
import com.koresuniku.wishmaster_v4.core.thread_list.interactor.ThreadListNetworkInteractor
import com.koresuniku.wishmaster_v4.core.thread_list.view.ThreadListAdapterView
import com.koresuniku.wishmaster_v4.core.thread_list.view.ThreadListView
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
        presenterData = ThreadListData.emptyData()
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