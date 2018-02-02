package com.koresuniku.wishmaster_v4.core.thread_list.view

import com.koresuniku.wishmaster_v4.core.base.mvp.IMvpView
import com.koresuniku.wishmaster_v4.core.data.model.threads.ThreadListData

/**
 * Created by koresuniku on 07.01.18.
 */

interface ThreadListAdapterView<P> : IMvpView<P> {
    fun onThreadListDataChanged(newThreadListData: ThreadListData)
}
