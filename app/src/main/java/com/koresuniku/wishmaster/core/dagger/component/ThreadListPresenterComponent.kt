package com.koresuniku.wishmaster.core.dagger.component

import com.koresuniku.wishmaster.application.listener.OrientationNotifier
import com.koresuniku.wishmaster.core.dagger.IWishmasterDaggerInjector
import com.koresuniku.wishmaster.core.dagger.module.RxModule
import com.koresuniku.wishmaster.core.dagger.module.thread_list_scopes.ThreadListPresenterModule
import com.koresuniku.wishmaster.core.dagger.scope.ForThreadListPresenter
import com.koresuniku.wishmaster.core.modules.thread_list.ThreadListAdapterViewInteractor
import com.koresuniku.wishmaster.core.modules.thread_list.ThreadListNetworkInteractor
import com.koresuniku.wishmaster.core.modules.thread_list.ThreadListPresenter
import com.koresuniku.wishmaster.core.utils.text.WishmasterTextUtils
import com.koresuniku.wishmaster.ui.anim.WishmasterAnimationUtils
import com.koresuniku.wishmaster.ui.utils.UiUtils
import dagger.Component
import io.reactivex.disposables.CompositeDisposable


@ForThreadListPresenter
@Component (dependencies = [ApplicationComponent::class],
        modules = [(ThreadListPresenterModule::class), (RxModule::class)])
interface ThreadListPresenterComponent {

    fun injector(): IWishmasterDaggerInjector
    fun threadListNetworkInteractor(): ThreadListNetworkInteractor
    fun threadListAdapterViewInteractor(): ThreadListAdapterViewInteractor
    fun textUtils(): WishmasterTextUtils
    fun uiUtils(): UiUtils
    fun orientationNotifier(): OrientationNotifier
    fun compositeDisposable(): CompositeDisposable
    fun animationUtils(): WishmasterAnimationUtils

    fun inject(threadListPresenter: ThreadListPresenter)
}