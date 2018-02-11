package com.koresuniku.wishmaster_v4.core.dagger.module

import com.koresuniku.wishmaster_v4.core.utils.search.ISearchInputMatcher
import com.koresuniku.wishmaster_v4.core.utils.search.SearchInputMatcher
import dagger.Module
import dagger.Provides


@Module
class SearchModule {

    @Provides
    fun provideSearchInputMatcher(): ISearchInputMatcher = SearchInputMatcher()
}