package com.koresuniku.wishmaster_v4.core.base.rx

import com.koresuniku.wishmaster_v4.application.shared_preferences.ISharedPreferencesStorage
import com.koresuniku.wishmaster_v4.core.base.interactor.ISharedPreferencesInteractor
import com.koresuniku.wishmaster_v4.core.base.mvp.IMvpPresenter
import io.reactivex.disposables.CompositeDisposable


abstract class BaseRxSharedPreferencesInteractor<P : IMvpPresenter<*>>(
        private val storage: ISharedPreferencesStorage,
        compositeDisposable: CompositeDisposable):
        BaseRxInteractor<P>(compositeDisposable), ISharedPreferencesInteractor {

    override fun getSharedPreferencesStorage(): ISharedPreferencesStorage = storage
}