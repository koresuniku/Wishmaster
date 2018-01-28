package com.koresuniku.wishmaster_v4.core.util.search

/**
 * Created by koresuniku on 03.01.18.
 */

data class SearchInputResponse(val responseCode: Int, val data: String) {
    companion object {
        val UNKNOWN_ADDRESS = "unknown_address"
        fun unknownResponse() = SearchInputResponse(SearchInputMatcher.UNKNOWN_CODE, UNKNOWN_ADDRESS)
    }
}