package com.koresuniku.wishmaster.core.dagger.component

import com.koresuniku.wishmaster.core.dagger.module.settings_scopes.SettingsViewModule
import com.koresuniku.wishmaster.core.dagger.scope.ForSettingsView
import com.koresuniku.wishmaster.ui.settings.SettingsActivity
import dagger.Component

/**
 * Created by koresuniku on 2/19/18.
 */

@ForSettingsView
@Component(dependencies = [(SettingsPresenterComponent::class)],
        modules = [(SettingsViewModule::class)])
interface SettingsViewComponent {

    fun inject(activity: SettingsActivity)

}