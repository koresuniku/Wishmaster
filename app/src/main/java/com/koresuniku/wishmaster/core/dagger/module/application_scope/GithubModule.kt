package com.koresuniku.wishmaster.core.dagger.module.application_scope

import com.koresuniku.wishmaster.core.network.github_api.GithubApiService
import com.koresuniku.wishmaster.core.network.github_api.GithubHelper
import com.koresuniku.wishmaster.core.network.github_api.GithubReleaseListNetworkInteractor
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Singleton

/**
 * Created by koresuniku on 2/18/18.
 */

@Module
class GithubModule {

    @Provides
    @Singleton
    fun provideGithubReleaseListNetworkInteractor(apiService: GithubApiService,
                                                  compositeDisposable: CompositeDisposable):
            GithubReleaseListNetworkInteractor {
        return GithubReleaseListNetworkInteractor(apiService, compositeDisposable)
    }

    @Provides
    @Singleton
    fun provideGithubHelper(networkInteractor: GithubReleaseListNetworkInteractor,
                            compositeDisposable: CompositeDisposable): GithubHelper {
        return GithubHelper(networkInteractor, compositeDisposable)
    }
}