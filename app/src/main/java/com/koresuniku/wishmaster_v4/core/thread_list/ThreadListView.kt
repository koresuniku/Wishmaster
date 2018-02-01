package com.koresuniku.wishmaster_v4.core.thread_list

import com.koresuniku.wishmaster_v4.core.base.mvp.MvpView

/**
 * Created by koresuniku on 01.01.18.
 */

interface ThreadListView<P> : MvpView<P> {
    fun getBoardId(): String
}