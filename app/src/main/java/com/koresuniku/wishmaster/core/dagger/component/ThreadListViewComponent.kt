package com.koresuniku.wishmaster.core.dagger.component

import com.koresuniku.wishmaster.core.dagger.module.RxModule
import com.koresuniku.wishmaster.core.dagger.module.thread_list_scopes.ThreadListViewModule
import com.koresuniku.wishmaster.core.dagger.scope.ForThreadListView
import com.koresuniku.wishmaster.ui.thread_list.ThreadListActivity
import com.koresuniku.wishmaster.ui.thread_list.ThreadListRecyclerViewAdapter
import dagger.Component

/**
 * Created by koresuniku on 01.01.18.
 */

@ForThreadListView
@Component(dependencies = [(ThreadListPresenterComponent::class)],
        modules = [(ThreadListViewModule::class)])
interface ThreadListViewComponent {

    fun inject(activity: ThreadListActivity)
    fun inject(threadListAdapterView: ThreadListRecyclerViewAdapter)

}