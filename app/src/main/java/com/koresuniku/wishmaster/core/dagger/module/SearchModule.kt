package com.koresuniku.wishmaster.core.dagger.module

import com.koresuniku.wishmaster.core.utils.search.ISearchInputMatcher
import com.koresuniku.wishmaster.core.utils.search.SearchInputMatcher
import dagger.Module
import dagger.Provides


@Module
class SearchModule {

    @Provides
    fun provideSearchInputMatcher(): ISearchInputMatcher = SearchInputMatcher()
}