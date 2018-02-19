package com.koresuniku.wishmaster.core.dagger.module.settings_scopes

import com.koresuniku.wishmaster.core.dagger.scope.ForSettingsView
import com.koresuniku.wishmaster.core.modules.settings.ISettingsPresenter
import com.koresuniku.wishmaster.core.modules.settings.SettingsPresenter
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by koresuniku on 2/19/18.
 */

@Module
class SettingsViewModule {

    @ForSettingsView
    @Provides
    fun provideSettingsPresenter(compositeDisposable: CompositeDisposable): ISettingsPresenter =
            SettingsPresenter(compositeDisposable)
}