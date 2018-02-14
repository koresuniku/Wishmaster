package com.koresuniku.wishmaster.core.dagger.component

import com.koresuniku.wishmaster.application.listener.OrientationNotifier
import com.koresuniku.wishmaster.core.dagger.IWishmasterDaggerInjector
import com.koresuniku.wishmaster.core.dagger.module.RxModule
import com.koresuniku.wishmaster.core.dagger.module.full_thread_scopes.FullThreadPresenterModule
import com.koresuniku.wishmaster.core.dagger.scope.ForFullThreadPresenter
import com.koresuniku.wishmaster.core.modules.full_thread.interactor.FullThreadAdapterViewInteractor
import com.koresuniku.wishmaster.core.modules.full_thread.interactor.FullThreadNetworkInteractor
import com.koresuniku.wishmaster.core.modules.full_thread.presenter.FullThreadPresenter
import com.koresuniku.wishmaster.core.utils.text.WishmasterTextUtils
import com.koresuniku.wishmaster.ui.anim.WishmasterAnimationUtils
import com.koresuniku.wishmaster.ui.utils.UiUtils
import dagger.Component
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by koresuniku on 2/11/18.
 */

@ForFullThreadPresenter
@Component (dependencies = [ApplicationComponent::class],
        modules = [(FullThreadPresenterModule::class), (RxModule::class)] )
interface FullThreadPresenterComponent {

    fun injector(): IWishmasterDaggerInjector
    fun fullThreadNetworkInteractor(): FullThreadNetworkInteractor
    fun fullThreadAdapterViewInteractor(): FullThreadAdapterViewInteractor
    fun textUtils(): WishmasterTextUtils
    fun uiUtils(): UiUtils
    fun orientationNotifier(): OrientationNotifier
    fun compositeDisposable(): CompositeDisposable
    fun animationUtils(): WishmasterAnimationUtils

    fun inject(fullThreadPresenter: FullThreadPresenter)
}