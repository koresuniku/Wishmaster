package com.koresuniku.wishmaster_v4.core.base.rx

import com.koresuniku.wishmaster_v4.core.base.interactor.ISearchInteractor
import com.koresuniku.wishmaster_v4.core.base.mvp.IMvpPresenter
import com.koresuniku.wishmaster_v4.core.util.search.ISearchInputMatcher
import io.reactivex.disposables.CompositeDisposable


abstract class BaseRxSearchInterator<P : IMvpPresenter<*>>(private val matcher: ISearchInputMatcher,
                                                           compositeDisposable: CompositeDisposable):
        BaseRxInteractorI<P>(compositeDisposable), ISearchInteractor {

    override fun getMatcher(): ISearchInputMatcher = matcher
}