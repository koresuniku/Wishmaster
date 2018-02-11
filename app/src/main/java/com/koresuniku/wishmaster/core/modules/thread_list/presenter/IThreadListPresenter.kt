package com.koresuniku.wishmaster.core.modules.thread_list.presenter

import com.koresuniku.wishmaster.core.base.mvp.IMvpPresenter
import com.koresuniku.wishmaster.core.modules.thread_list.view.ThreadItemView
import com.koresuniku.wishmaster.core.modules.thread_list.view.ThreadListAdapterView
import com.koresuniku.wishmaster.core.modules.thread_list.view.ThreadListView


interface IThreadListPresenter : IMvpPresenter<ThreadListView<IThreadListPresenter>> {
    var threadListAdapterView: ThreadListAdapterView<IThreadListPresenter>?

    fun loadThreadList()
    fun getBoardId(): String
    fun getThreadListDataSize(): Int
    fun getThreadItemType(position: Int): Int
    fun setItemViewData(threadItemView: ThreadItemView, position: Int)

    fun onNetworkError(t: Throwable)

    fun bindThreadListAdapterView(threadListAdapterView: ThreadListAdapterView<IThreadListPresenter>)
    fun unbindThreadListAdapterView()
}