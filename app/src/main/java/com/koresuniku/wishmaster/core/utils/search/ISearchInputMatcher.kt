package com.koresuniku.wishmaster.core.utils.search


interface ISearchInputMatcher {
    fun matchInput(input: String): SearchInputResponse
    fun checkIfBoard(input: String): SearchInputResponse
    fun checkIfThread(input: String): SearchInputResponse
    fun checkIfPost(input: String): SearchInputResponse
}