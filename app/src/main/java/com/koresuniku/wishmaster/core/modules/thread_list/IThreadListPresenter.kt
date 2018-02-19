package com.koresuniku.wishmaster.core.modules.thread_list

import com.koresuniku.wishmaster.core.base.mvp.IMvpDataPresenter
import com.koresuniku.wishmaster.core.base.mvp.IMvpPresenter
import com.koresuniku.wishmaster.core.data.model.threads.ThreadListData


interface IThreadListPresenter : IMvpPresenter<ThreadListView<IThreadListPresenter>>,
        IMvpDataPresenter<ThreadListData> {
    var threadListAdapterView: ThreadListAdapterView<IThreadListPresenter>?

    fun loadThreadList()
    fun getBoardId(): String
    fun getThreadListDataSize(): Int
    fun getThreadItemType(position: Int): Int
    fun setItemViewData(threadItemView: ThreadItemView, position: Int)
    fun onThreadItemClicked(threadNumber: String)

    fun onNetworkError(t: Throwable)

    fun bindThreadListAdapterView(threadListAdapterView: ThreadListAdapterView<IThreadListPresenter>)
    fun unbindThreadListAdapterView()
}