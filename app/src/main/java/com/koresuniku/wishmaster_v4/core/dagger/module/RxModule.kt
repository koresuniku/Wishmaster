package com.koresuniku.wishmaster_v4.core.dagger.module

import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable


@Module
class RxModule {

    @Provides
    fun provideCompositeDisposable(): CompositeDisposable = CompositeDisposable()
}