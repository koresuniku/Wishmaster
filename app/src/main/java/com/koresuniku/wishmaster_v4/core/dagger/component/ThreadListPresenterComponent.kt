package com.koresuniku.wishmaster_v4.core.dagger.component

import com.koresuniku.wishmaster_v4.core.dagger.IWishmasterDaggerInjector
import com.koresuniku.wishmaster_v4.core.dagger.module.RxModule
import com.koresuniku.wishmaster_v4.core.dagger.module.thread_list_scopes.ThreadListPresenterModule
import com.koresuniku.wishmaster_v4.core.dagger.scope.ForThreadListPresenter
import com.koresuniku.wishmaster_v4.core.thread_list.interactor.ThreadListAdapterViewInteractor
import com.koresuniku.wishmaster_v4.core.thread_list.interactor.ThreadListNetworkInteractor
import com.koresuniku.wishmaster_v4.core.thread_list.presenter.ThreadListPresenter
import dagger.Component


@ForThreadListPresenter
@Component (dependencies = [ApplicationComponent::class],
        modules = [(ThreadListPresenterModule::class), (RxModule::class)])
interface ThreadListPresenterComponent {

    fun injector(): IWishmasterDaggerInjector
    fun threadListNetworkInteractor(): ThreadListNetworkInteractor
    fun threadListAdapterViewInteractor(): ThreadListAdapterViewInteractor

    fun inject(threadListPresenter: ThreadListPresenter)
}