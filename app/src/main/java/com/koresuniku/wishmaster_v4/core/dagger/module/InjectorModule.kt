package com.koresuniku.wishmaster_v4.core.dagger.module

import com.koresuniku.wishmaster_v4.core.dagger.IWishmasterInjector
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class InjectorModule(private val injector: IWishmasterInjector) {

    @Provides
@Singleton
fun provideInjector(): IWishmasterInjector = injector
}