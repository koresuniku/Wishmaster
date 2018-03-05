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

package com.koresuniku.wishmaster.core.modules.full_thread

import com.koresuniku.wishmaster.application.notifier.OnOrientationChangedListener
import com.koresuniku.wishmaster.core.base.IMvpView
import com.koresuniku.wishmaster.core.data.model.posts.PostListData

/**
 * Created by koresuniku on 2/14/18.
 */

interface FullThreadAdapterView<P> : IMvpView<P>, OnOrientationChangedListener {
    val NO_IMAGES_CODE: Int
    val SINGLE_IMAGE_CODE: Int
    val MULTIPLE_IMAGES_CODE: Int
    fun onPostListDataChanged(newPostListData: PostListData)
    fun onNewPostsReceived(oldCount: Int, newCount: Int)
}