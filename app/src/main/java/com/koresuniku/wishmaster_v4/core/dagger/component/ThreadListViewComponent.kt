package com.koresuniku.wishmaster_v4.core.dagger.component

import com.koresuniku.wishmaster_v4.core.dagger.scope.ForDashboardView
import com.koresuniku.wishmaster_v4.core.dagger.module.thread_list_scope.ThreadListModule
import com.koresuniku.wishmaster_v4.core.thread_list.ThreadListPresenter
import com.koresuniku.wishmaster_v4.ui.thread_list.ThreadListActivity
import com.koresuniku.wishmaster_v4.ui.thread_list.ThreadListRecyclerViewAdapter
import dagger.Component

/**
 * Created by koresuniku on 01.01.18.
 */

@ForDashboardView
@Component(dependencies = [(ApplicationComponent::class)], modules = [(ThreadListModule::class)])
interface ThreadListViewComponent {

    fun inject(presenter: ThreadListPresenter)
    fun inject(activity: ThreadListActivity)
    fun inject(threadListAdapterView: ThreadListRecyclerViewAdapter)

}