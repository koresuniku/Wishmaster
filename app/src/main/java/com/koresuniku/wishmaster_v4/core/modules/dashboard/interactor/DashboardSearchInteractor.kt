package com.koresuniku.wishmaster_v4.core.modules.dashboard.interactor

import com.koresuniku.wishmaster_v4.core.base.rx.BaseRxSearchInteractor
import com.koresuniku.wishmaster_v4.core.modules.dashboard.presenter.IDashboardPresenter
import com.koresuniku.wishmaster_v4.core.utils.search.ISearchInputMatcher
import com.koresuniku.wishmaster_v4.core.utils.search.SearchInputResponse
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable


class DashboardSearchInteractor(
        matcher: ISearchInputMatcher,
        compositeDisposable: CompositeDisposable):
        BaseRxSearchInteractor<IDashboardPresenter>(matcher, compositeDisposable) {

    override fun processInput(input: String): Single<SearchInputResponse> {
        return Single.create({
            val response = getMatcher().matchInput(input)
            it.onSuccess(response)
        })
    }
}