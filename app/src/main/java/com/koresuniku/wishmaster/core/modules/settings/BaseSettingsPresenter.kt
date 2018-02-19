package com.koresuniku.wishmaster.core.modules.settings

import com.koresuniku.wishmaster.core.base.rx.BaseRxPresenter
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by koresuniku on 2/19/18.
 */

abstract class BaseSettingsPresenter(compositeDisposable: CompositeDisposable) :
        BaseRxPresenter<SettingsView<ISettingsPresenter>>(compositeDisposable),
        ISettingsPresenter {


}