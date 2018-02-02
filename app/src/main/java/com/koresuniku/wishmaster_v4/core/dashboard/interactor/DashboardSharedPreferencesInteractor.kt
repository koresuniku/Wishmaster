package com.koresuniku.wishmaster_v4.core.dashboard.interactor

import com.koresuniku.wishmaster_v4.application.shared_preferences.ISharedPreferencesStorage
import com.koresuniku.wishmaster_v4.application.shared_preferences.SharedPreferencesKeystore
import com.koresuniku.wishmaster_v4.core.base.rx.BaseRxSharedPreferencesInteractorI
import com.koresuniku.wishmaster_v4.core.dashboard.presenter.IDashboardPresenter
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class DashboardSharedPreferencesInteractor(
        storage: ISharedPreferencesStorage,
        compositeDisposable: CompositeDisposable):
        BaseRxSharedPreferencesInteractorI<IDashboardPresenter>(storage, compositeDisposable) {

    fun getDashboardFavouriteTabPosition(): Single<Int> {
        return Single.create({
            getSharedPreferencesStorage().readInt(
                    SharedPreferencesKeystore.DASHBOARD_PREFERRED_TAB_POSITION_KEY,
                    SharedPreferencesKeystore.DASHBOARD_PREFERRED_TAB_POSITION_DEFAULT)
                    .observeOn(Schedulers.io())
                    .subscribe(it::onSuccess)
        })
    }
}