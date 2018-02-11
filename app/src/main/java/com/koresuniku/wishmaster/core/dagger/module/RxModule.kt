package com.koresuniku.wishmaster.core.dagger.module

import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable


@Module
class RxModule {

    @Provides
    fun provideCompositeDisposable(): CompositeDisposable = CompositeDisposable()
}