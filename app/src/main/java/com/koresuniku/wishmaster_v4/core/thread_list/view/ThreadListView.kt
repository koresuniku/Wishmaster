package com.koresuniku.wishmaster_v4.core.thread_list.view

import com.koresuniku.wishmaster_v4.core.base.mvp.IMvpView

/**
 * Created by koresuniku on 01.01.18.
 */

interface ThreadListView<P> : IMvpView<P> {
    fun getBoardId(): String
    fun onThreadListReceived(boardName: String)
    fun showThreadList()
    fun showError(message: String?)
}