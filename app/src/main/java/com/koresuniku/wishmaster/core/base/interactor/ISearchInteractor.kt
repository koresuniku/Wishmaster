package com.koresuniku.wishmaster.core.base.interactor

import com.koresuniku.wishmaster.core.utils.search.ISearchInputMatcher
import com.koresuniku.wishmaster.core.utils.search.SearchInputResponse
import io.reactivex.Single


interface ISearchInteractor {
    fun getMatcher(): ISearchInputMatcher
    fun processInput(input: String): Single<SearchInputResponse>
}