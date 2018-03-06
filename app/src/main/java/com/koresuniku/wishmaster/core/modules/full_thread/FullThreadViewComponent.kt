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

import com.koresuniku.wishmaster.ui.full_thread.FullThreadActivity
import com.koresuniku.wishmaster.ui.full_thread.FullThreadRecyclerViewAdapter
import com.koresuniku.wishmaster.ui.full_thread.PostItemViewHolder
import dagger.Component

/**
 * Created by koresuniku on 2/11/18.
 */

@FullThreadScopes.ForFullThreadView
@Component (dependencies = [FullThreadPresenterComponent::class],
        modules = [(FullThreadViewModule::class)])
interface FullThreadViewComponent {

    fun inject(fullThreadActivity: FullThreadActivity)
    fun inject(postItemViewHolder: PostItemViewHolder)
    fun inject(fullThreadAdapterView: FullThreadRecyclerViewAdapter)
}