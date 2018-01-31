package com.koresuniku.wishmaster_v4.core.dashboard

import com.koresuniku.wishmaster_v4.core.base.mvp.MvpPresenter
import com.koresuniku.wishmaster_v4.core.base.rx.BaseRxSearchInterator
import com.koresuniku.wishmaster_v4.core.util.search.ISearchInputMatcher
import com.koresuniku.wishmaster_v4.core.util.search.SearchInputMatcher
import com.koresuniku.wishmaster_v4.core.util.search.SearchInputResponse
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable


class DashboardSearchInteractor(
        matcher: ISearchInputMatcher,
        compositeDisposable: CompositeDisposable):
        BaseRxSearchInterator<IDashboardPresenter>(matcher, compositeDisposable) {

    override fun processInput(input: String): Single<SearchInputResponse> {
        return Single.create({
            val response = getMatcher().matchInput(input)
            it.onSuccess(response)
        })
    }
}