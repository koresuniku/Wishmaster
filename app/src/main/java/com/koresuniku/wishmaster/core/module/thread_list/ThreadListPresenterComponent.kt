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

import com.koresuniku.wishmaster.application.IWishmasterDependencyInjector
import com.koresuniku.wishmaster.core.module.gallery.GalleryPresenter
import com.koresuniku.wishmaster.core.module.gallery.GalleryPresenterModule
import com.koresuniku.wishmaster.core.module.gallery.IGalleryPresenterComponent
import com.koresuniku.wishmaster.core.module.gallery.IGalleryViewComponent
import dagger.Component

/**
 * Created by koresuniku on 3/6/18.
 */

@ThreadListScopes.ForThreadListPresenter
@Component(dependencies = [(ThreadListLogicComponent::class)],
        modules = [(GalleryPresenterModule::class)])
interface ThreadListPresenterComponent : IGalleryPresenterComponent {
    fun injector(): IWishmasterDependencyInjector
    fun galleryViewComponent(): IGalleryViewComponent

    fun inject(threadListPresenter: ThreadListPresenter)
}