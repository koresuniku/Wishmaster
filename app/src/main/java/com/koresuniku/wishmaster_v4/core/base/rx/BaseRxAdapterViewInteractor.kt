package com.koresuniku.wishmaster_v4.core.base.rx

import com.koresuniku.wishmaster_v4.core.base.interactor.IAdapterViewInteractor
import com.koresuniku.wishmaster_v4.core.base.mvp.IMvpPresenter
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by koresuniku on 02.02.18.
 */
abstract class BaseRxAdapterViewInteractor<P : IMvpPresenter<*>, A, V, D>(
        compositeDisposable: CompositeDisposable):
        BaseRxInteractor<P>(compositeDisposable), IAdapterViewInteractor<A, V, D>