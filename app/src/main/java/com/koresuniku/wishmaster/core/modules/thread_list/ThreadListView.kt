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

package com.koresuniku.wishmaster.core.modules.thread_list

import com.koresuniku.wishmaster.core.base.mvp.IMvpView

/**
 * Created by koresuniku on 01.01.18.
 */

interface ThreadListView<P> : IMvpView<P> {
    fun getBoardId(): String
    fun onThreadListReceived(boardName: String)
    fun showError(message: String?)
    fun showLoading()
    fun launchFullThreadActivity(threadNumber: String)
}