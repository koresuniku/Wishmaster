package com.koresuniku.wishmaster_v4.core.dagger.module.application_scope

import com.koresuniku.wishmaster_v4.core.dagger.IWishmasterDaggerInjector
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class InjectorModule(private val injector: IWishmasterDaggerInjector) {

    @Provides
@Singleton
fun provideInjector(): IWishmasterDaggerInjector = injector
}