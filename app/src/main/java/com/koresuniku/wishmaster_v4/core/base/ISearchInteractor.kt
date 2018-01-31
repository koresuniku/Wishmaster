package com.koresuniku.wishmaster_v4.core.base

import com.koresuniku.wishmaster_v4.core.util.search.ISearchInputMatcher
import com.koresuniku.wishmaster_v4.core.util.search.SearchInputResponse
import io.reactivex.Single


interface ISearchInteractor {
    fun getMatcher(): ISearchInputMatcher
    fun processInput(input: String): Single<SearchInputResponse>
}