package com.koresuniku.wishmaster.core.base.rx

import com.koresuniku.wishmaster.application.preferences.ISharedPreferencesStorage
import com.koresuniku.wishmaster.core.base.interactor.ISharedPreferencesInteractor
import com.koresuniku.wishmaster.core.base.mvp.IMvpPresenter
import io.reactivex.disposables.CompositeDisposable


abstract class BaseRxSharedPreferencesInteractor<P : IMvpPresenter<*>>(
        private val storage: ISharedPreferencesStorage,
        compositeDisposable: CompositeDisposable):
        BaseRxInteractor<P>(compositeDisposable), ISharedPreferencesInteractor {

    override fun getSharedPreferencesStorage(): ISharedPreferencesStorage = storage
}