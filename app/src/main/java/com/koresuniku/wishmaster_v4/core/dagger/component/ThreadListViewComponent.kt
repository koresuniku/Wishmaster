package com.koresuniku.wishmaster_v4.core.dagger.component

import com.koresuniku.wishmaster_v4.core.dagger.module.RxModule
import com.koresuniku.wishmaster_v4.core.dagger.module.thread_list_scopes.ThreadListViewModule
import com.koresuniku.wishmaster_v4.core.dagger.scope.ForThreadListView
import com.koresuniku.wishmaster_v4.ui.thread_list.ThreadListActivity
import com.koresuniku.wishmaster_v4.ui.thread_list.ThreadListRecyclerViewAdapter
import dagger.Component

/**
 * Created by koresuniku on 01.01.18.
 */

@ForThreadListView
@Component(dependencies = [(ThreadListPresenterComponent::class)], modules = [(ThreadListViewModule::class), (RxModule::class)])
interface ThreadListViewComponent {

    fun inject(activity: ThreadListActivity)
    fun inject(threadListAdapterView: ThreadListRecyclerViewAdapter)

}