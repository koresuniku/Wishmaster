package com.koresuniku.wishmaster_v4.core.dashboard

import com.koresuniku.wishmaster_v4.application.ISharedPreferencesStorage
import com.koresuniku.wishmaster_v4.application.SharedPreferencesKeystore
import com.koresuniku.wishmaster_v4.core.base.rx.BaseRxSharedPreferencesInteractor
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class DashboardSharedPreferencesInteractor(
        storage: ISharedPreferencesStorage,
        compositeDisposable: CompositeDisposable):
        BaseRxSharedPreferencesInteractor<IDashboardPresenter>(storage, compositeDisposable) {

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