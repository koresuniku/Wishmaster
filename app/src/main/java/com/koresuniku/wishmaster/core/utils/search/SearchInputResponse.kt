/*
 * Copyright (c) 2018 koresuniku
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.koresuniku.wishmaster.core.utils.search

/**
 * Created by koresuniku on 03.01.18.
 */

data class SearchInputResponse(val responseCode: Int, val data: String) {
    companion object {
        const val UNKNOWN_ADDRESS = "unknown_address"
        fun unknownResponse() = SearchInputResponse(SearchInputMatcher.UNKNOWN_CODE, UNKNOWN_ADDRESS)
    }
}