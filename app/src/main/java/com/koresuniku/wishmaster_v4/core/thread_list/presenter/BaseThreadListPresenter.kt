package com.koresuniku.wishmaster_v4.core.thread_list.presenter

import com.koresuniku.wishmaster_v4.core.base.rx.BaseRxPresenter
import com.koresuniku.wishmaster_v4.core.thread_list.view.ThreadListAdapterView
import com.koresuniku.wishmaster_v4.core.thread_list.view.ThreadListView

abstract class BaseThreadListPresenter : BaseRxPresenter<ThreadListView<IThreadListPresenter>>(), IThreadListPresenter {

    protected var threadListAdapterView: ThreadListAdapterView<IThreadListPresenter>? = null


    override fun bindThreadListAdapterView(threadListAdapterView: ThreadListAdapterView<IThreadListPresenter>) {
        this.threadListAdapterView = threadListAdapterView
    }

    override fun unbindThreadListAdapterView() {
        this.threadListAdapterView = null
    }


}