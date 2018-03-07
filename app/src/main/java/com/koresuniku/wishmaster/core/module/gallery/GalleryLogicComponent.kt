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

package com.koresuniku.wishmaster.core.module.gallery

import com.koresuniku.wishmaster.application.ApplicationComponent
import com.koresuniku.wishmaster.application.IWishmasterDependencyInjector
import com.koresuniku.wishmaster.application.global.RxModule
import dagger.Component
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by koresuniku on 3/7/18.
 */

@GalleryScopes.ForGalleryLogic
@Component(
        dependencies = [ApplicationComponent::class],
        modules = [(GalleryLogicModule::class), (RxModule::class)])
interface GalleryLogicComponent {

    //Global singletons
    fun injector(): IWishmasterDependencyInjector
    fun compositeDisposable(): CompositeDisposable

    //Interactor
    fun galleryInteractor(): GalleryContract.IGalleryInteractor

    fun inject(galleryInteractor: GalleryInteractor)

}