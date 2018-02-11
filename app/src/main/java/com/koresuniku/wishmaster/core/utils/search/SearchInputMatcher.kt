package com.koresuniku.wishmaster.core.utils.search

import com.koresuniku.wishmaster.core.network.Dvach
import java.util.regex.Pattern
import javax.inject.Inject

/**
 * Created by koresuniku on 02.01.18.
 */

class SearchInputMatcher @Inject constructor(): ISearchInputMatcher {

    companion object {
        const val UNKNOWN_CODE = -1
        const val BOARD_CODE = 0
        const val THREAD_CODE = 1
        const val POST_CODE = 2
    }

    override fun matchInput(input: String): SearchInputResponse {
        var result = checkIfBoard(input).data
        if (result != SearchInputResponse.UNKNOWN_ADDRESS) return SearchInputResponse(BOARD_CODE, result)

        result = checkIfThread(input).data
        if (result != SearchInputResponse.UNKNOWN_ADDRESS) return SearchInputResponse(THREAD_CODE, result)

        result = checkIfPost(input).data
        if (result != SearchInputResponse.UNKNOWN_ADDRESS) return SearchInputResponse(POST_CODE, result)

        return SearchInputResponse.unknownResponse()
    }

    override fun checkIfBoard(input: String): SearchInputResponse {
        Dvach.MIRRORS.forEach {
            val pattern = Pattern.compile("(^(https?://)?(www\\.)?$it/+)?/*[a-zA-Z0-9]+/*")
            val matcher = pattern.matcher(input)
            return if (matcher.matches()) {
                val boardData = input.replace(Regex("/+"), "")
                SearchInputResponse(BOARD_CODE, boardData)
            } else SearchInputResponse(UNKNOWN_CODE, SearchInputResponse.UNKNOWN_ADDRESS)
        }
        return SearchInputResponse.unknownResponse()
    }

    override fun checkIfThread(input: String): SearchInputResponse {
        Dvach.MIRRORS.forEach {
            val pattern = Pattern.compile("^(https?://)?(www\\.)?$it/+[a-zA-Z0-9]+/+res/+[0-9]+\\.html$")
            val matcher = pattern.matcher(input)
            if (matcher.matches()) {
                val threadData = input
                        .replace(Regex("^(https?://)?(www\\.)?$it/+[a-zA-Z0-9]+/+res/+"), "")
                        .replace(Regex("\\.html"), "")
                        .replace(Regex("/+"), "")
                return SearchInputResponse(THREAD_CODE, threadData)
            }
        }
        return SearchInputResponse.unknownResponse()
    }

    override fun checkIfPost(input: String): SearchInputResponse {
        Dvach.MIRRORS.forEach {
            val pattern = Pattern.compile("^(https?://)?(www\\.)?$it/+[a-zA-Z0-9]+/+res/+[0-9]+\\.html#[0-9]+$")
            val matcher = pattern.matcher(input)
            if (matcher.matches()) {
                val postData = input
                        .replace(Regex("^(https?://)?(www\\.)?$it/+[a-zA-Z0-9]+/+res/+"), "")
                        .replace(Regex("\\.html[0-9]+\$"), "")
                        .replace(Regex("/+"), "")
                return SearchInputResponse(POST_CODE, postData)
            }
        }
        return SearchInputResponse.unknownResponse()
    }

}