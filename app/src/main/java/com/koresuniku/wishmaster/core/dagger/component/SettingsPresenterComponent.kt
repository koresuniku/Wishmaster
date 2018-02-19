package com.koresuniku.wishmaster.core.dagger.component

import com.koresuniku.wishmaster.core.dagger.IWishmasterDaggerInjector
import com.koresuniku.wishmaster.core.dagger.module.RxModule
import com.koresuniku.wishmaster.core.dagger.module.settings_scopes.SettingsPresenterModule
import com.koresuniku.wishmaster.core.dagger.scope.ForSettingsPresenter
import com.koresuniku.wishmaster.core.modules.settings.SettingsPresenter
import dagger.Component
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by koresuniku on 2/19/18.
 */

@ForSettingsPresenter
@Component(dependencies = [ApplicationComponent::class],
        modules = [(SettingsPresenterModule::class), (RxModule::class)])
interface SettingsPresenterComponent {

    fun injector(): IWishmasterDaggerInjector
    fun compositeDisposable(): CompositeDisposable
    fun inject(settingsPresenter: SettingsPresenter)

}