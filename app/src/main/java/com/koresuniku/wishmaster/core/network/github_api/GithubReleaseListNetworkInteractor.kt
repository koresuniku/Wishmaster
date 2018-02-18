package com.koresuniku.wishmaster.core.network.github_api

import com.koresuniku.wishmaster.core.base.interactor.INetworkInteractor
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by koresuniku on 2/18/18.
 */

class GithubReleaseListNetworkInteractor @Inject constructor(private val apiService: GithubApiService,
                                                             private val compositeDisposable: CompositeDisposable):
        INetworkInteractor<GithubApiService, List<Release>> {

    override fun getService() = apiService

    override fun getDataFromNetwork(): Single<List<Release>> {
        return Single.create({ e ->
            compositeDisposable.add(getService()
                    .getRealeaseList()
                    .subscribeOn(Schedulers.newThread())
                    .subscribe({ e.onSuccess(it) }, { it.printStackTrace() }))
        })
    }
}