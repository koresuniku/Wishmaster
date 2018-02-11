package com.koresuniku.wishmaster_v4.core.modules.full_thread.presenter

import com.koresuniku.wishmaster_v4.application.listener.OrientationNotifier
import com.koresuniku.wishmaster_v4.core.dagger.IWishmasterDaggerInjector
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/**
 * Created by koresuniku on 2/11/18.
 */
class FullThreadPresener @Inject constructor(private val injector: IWishmasterDaggerInjector,
                                             compositeDisposable: CompositeDisposable,
                                             orientationNotifier: OrientationNotifier) : BaseFullThreadPresenter(compositeDisposable, orientationNotifier) {
    private val LOG_TAG = FullThreadPresener::class.simpleName
}