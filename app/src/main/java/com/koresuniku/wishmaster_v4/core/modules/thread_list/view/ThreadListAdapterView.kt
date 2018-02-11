package com.koresuniku.wishmaster_v4.core.modules.thread_list.view

import com.koresuniku.wishmaster_v4.application.listener.OnOrientationChangedListener
import com.koresuniku.wishmaster_v4.core.base.mvp.IMvpView
import com.koresuniku.wishmaster_v4.core.data.model.threads.ThreadListData

/**
 * Created by koresuniku on 07.01.18.
 */

interface ThreadListAdapterView<P> : IMvpView<P>, OnOrientationChangedListener {
    val NO_IMAGES_CODE: Int
    val SINGLE_IMAGE_CODE: Int
    val MULTIPLE_IMAGES_CODE: Int
    fun onThreadListDataChanged(newThreadListData: ThreadListData)
}
