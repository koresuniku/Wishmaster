package com.koresuniku.wishmaster_v4.core.dagger.component

import com.koresuniku.wishmaster_v4.core.dagger.IWishmasterDaggerInjector
import com.koresuniku.wishmaster_v4.core.dagger.module.thread_list_scope.ThreadListPresenterModule
import com.koresuniku.wishmaster_v4.core.dagger.scope.ForThreadListPresenter
import com.koresuniku.wishmaster_v4.core.thread_list.ThreadListPresenter
import dagger.Component


@ForThreadListPresenter
@Component (dependencies = [ApplicationComponent::class], modules = [ThreadListPresenterModule::class])
interface ThreadListPresenterComponent {

    fun injector(): IWishmasterDaggerInjector

    fun inject(threadListPresenter: ThreadListPresenter)
}