package com.koresuniku.wishmaster_v4.core.thread_list

import com.koresuniku.wishmaster_v4.core.base.IActivityMvpView

/**
 * Created by koresuniku on 01.01.18.
 */

interface ThreadListView : IActivityMvpView {
    fun getBoardId(): String
}