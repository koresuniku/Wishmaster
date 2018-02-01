package com.koresuniku.wishmaster_v4.core.dashboard.interactor

import com.koresuniku.wishmaster_v4.core.base.rx.BaseRxSearchInterator
import com.koresuniku.wishmaster_v4.core.dashboard.presenter.IDashboardPresenterI
import com.koresuniku.wishmaster_v4.core.util.search.ISearchInputMatcher
import com.koresuniku.wishmaster_v4.core.util.search.SearchInputResponse
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable


class DashboardSearchInteractor(
        matcher: ISearchInputMatcher,
        compositeDisposable: CompositeDisposable):
        BaseRxSearchInterator<IDashboardPresenterI>(matcher, compositeDisposable) {

    override fun processInput(input: String): Single<SearchInputResponse> {
        return Single.create({
            val response = getMatcher().matchInput(input)
            it.onSuccess(response)
        })
    }
}