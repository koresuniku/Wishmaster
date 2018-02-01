package com.koresuniku.wishmaster_v4.core.thread_list

import com.koresuniku.wishmaster_v4.core.base.mvp.MvpView
import com.koresuniku.wishmaster_v4.core.data.threads.ThreadListData

/**
 * Created by koresuniku on 07.01.18.
 */

interface ThreadListAdapterView<P> : MvpView<P> {
    fun onThreadListDataChanged(newThreadListData: ThreadListData)
}
