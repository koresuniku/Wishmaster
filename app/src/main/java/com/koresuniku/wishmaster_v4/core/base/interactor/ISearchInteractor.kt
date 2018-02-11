package com.koresuniku.wishmaster_v4.core.base.interactor

import com.koresuniku.wishmaster_v4.core.utils.search.ISearchInputMatcher
import com.koresuniku.wishmaster_v4.core.utils.search.SearchInputResponse
import io.reactivex.Single


interface ISearchInteractor {
    fun getMatcher(): ISearchInputMatcher
    fun processInput(input: String): Single<SearchInputResponse>
}