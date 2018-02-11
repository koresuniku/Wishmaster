package com.koresuniku.wishmaster.core.base.rx

import com.koresuniku.wishmaster.core.base.interactor.ISearchInteractor
import com.koresuniku.wishmaster.core.base.mvp.IMvpPresenter
import com.koresuniku.wishmaster.core.utils.search.ISearchInputMatcher
import io.reactivex.disposables.CompositeDisposable


abstract class BaseRxSearchInteractor<P : IMvpPresenter<*>>(private val matcher: ISearchInputMatcher,
                                                            compositeDisposable: CompositeDisposable):
        BaseRxInteractor<P>(compositeDisposable), ISearchInteractor {

    override fun getMatcher(): ISearchInputMatcher = matcher
}