package com.koresuniku.wishmaster.core.dagger.module.application_scope

import com.koresuniku.wishmaster.core.dagger.IWishmasterDaggerInjector
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class InjectorModule(private val injector: IWishmasterDaggerInjector) {

    @Provides
    @Singleton
    fun provideInjector(): IWishmasterDaggerInjector = injector
}