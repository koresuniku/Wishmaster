package com.koresuniku.wishmaster.core.modules.full_thread.presenter

import com.koresuniku.wishmaster.application.listener.OrientationNotifier
import com.koresuniku.wishmaster.core.dagger.IWishmasterDaggerInjector
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/**
 * Created by koresuniku on 2/11/18.
 */
class FullThreadPresenter @Inject constructor(private val injector: IWishmasterDaggerInjector,
                                              compositeDisposable: CompositeDisposable,
                                              orientationNotifier: OrientationNotifier) : BaseFullThreadPresenter(compositeDisposable, orientationNotifier) {
   
}