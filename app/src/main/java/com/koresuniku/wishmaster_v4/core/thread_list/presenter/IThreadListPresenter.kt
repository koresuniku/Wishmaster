package com.koresuniku.wishmaster_v4.core.thread_list.presenter

import com.koresuniku.wishmaster_v4.core.base.mvp.IMvpPresenter
import com.koresuniku.wishmaster_v4.core.thread_list.view.ThreadListAdapterView
import com.koresuniku.wishmaster_v4.core.thread_list.view.ThreadListView


interface IThreadListPresenter : IMvpPresenter<ThreadListView<IThreadListPresenter>> {
    var threadListAdapterView: ThreadListAdapterView<IThreadListPresenter>?

    fun loadThreadList()
    fun getBoardId(): String
    fun getThreadListDataSize(): Int

    fun bindThreadListAdapterView(threadListAdapterView: ThreadListAdapterView<IThreadListPresenter>)
    fun unbindThreadListAdapterView()
}