package com.koresuniku.wishmaster.core.modules.dashboard

import com.koresuniku.wishmaster.core.base.rx.BaseRxSearchInteractor
import com.koresuniku.wishmaster.core.utils.search.ISearchInputMatcher
import com.koresuniku.wishmaster.core.utils.search.SearchInputResponse
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