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

package com.koresuniku.wishmaster.core.module.thread_list

import com.koresuniku.wishmaster.core.module.gallery.GalleryViewModule
import com.koresuniku.wishmaster.core.module.gallery.IGalleryViewComponent
import com.koresuniku.wishmaster.ui.gallery.GalleryFragment
import com.koresuniku.wishmaster.ui.gallery.GalleryImageFragment
import com.koresuniku.wishmaster.ui.gallery.GalleryPagerAdapter
import com.koresuniku.wishmaster.ui.gallery.PreviewImageGridAdapter
import com.koresuniku.wishmaster.ui.thread_list.ThreadItemViewHolder
import com.koresuniku.wishmaster.ui.thread_list.ThreadListActivity
import com.koresuniku.wishmaster.ui.thread_list.ThreadListRecyclerViewAdapter
import dagger.Component

/**
 * Created by koresuniku on 01.01.18.
 */

@ThreadListScopes.ForThreadListView
@Component(dependencies = [ThreadListPresenterComponent::class],
        modules = [(ThreadListViewModule::class), (GalleryViewModule::class)])
interface ThreadListViewComponent : IGalleryViewComponent {

    fun inject(activity: ThreadListActivity)
    fun inject(threadListAdapterView: ThreadListRecyclerViewAdapter)
    fun inject(threadItemViewHolder: ThreadItemViewHolder)
    fun inject(previewImageGridAdapter: PreviewImageGridAdapter)
}