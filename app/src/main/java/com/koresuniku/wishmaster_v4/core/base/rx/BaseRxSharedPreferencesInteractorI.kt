package com.koresuniku.wishmaster_v4.core.base.rx

import com.koresuniku.wishmaster_v4.application.ISharedPreferencesStorage
import com.koresuniku.wishmaster_v4.core.base.interactor.ISharedPreferencesInteractor
import com.koresuniku.wishmaster_v4.core.base.mvp.IMvpPresenter
import io.reactivex.disposables.CompositeDisposable


abstract class BaseRxSharedPreferencesInteractorI<P : IMvpPresenter<*>>(
        private val storage: ISharedPreferencesStorage,
        compositeDisposable: CompositeDisposable):
        BaseRxInteractorI<P>(compositeDisposable), ISharedPreferencesInteractor {

    override fun getSharedPreferencesStorage(): ISharedPreferencesStorage = storage
}