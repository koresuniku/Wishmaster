package com.koresuniku.wishmaster.core.dagger.component

import com.koresuniku.wishmaster.core.dagger.module.RxModule
import com.koresuniku.wishmaster.core.dagger.module.full_thread_scopes.FullThreadViewModule
import com.koresuniku.wishmaster.core.dagger.scope.ForFullThreadView
import com.koresuniku.wishmaster.ui.full_thread.FullThreadActivity
import com.koresuniku.wishmaster.ui.full_thread.FullThreadRecyclerViewAdapter
import com.koresuniku.wishmaster.ui.full_thread.PostItemViewHolder
import dagger.Component

/**
 * Created by koresuniku on 2/11/18.
 */

@ForFullThreadView
@Component (dependencies = [FullThreadPresenterComponent::class],
        modules = [(FullThreadViewModule::class)])
interface FullThreadViewComponent {

    fun inject(fullThreadActivity: FullThreadActivity)
    fun inject(postItemViewHolder: PostItemViewHolder)
    fun inject(fullThreadAdapterView: FullThreadRecyclerViewAdapter)
}