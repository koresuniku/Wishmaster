package com.koresuniku.wishmaster.core.network.github_api

import io.reactivex.Observable
import retrofit2.http.GET

/**
 * Created by koresuniku on 2/18/18.
 */

interface GithubApiService {

    @GET("/repos/koresuniku/wishmaster/releases")
    fun getRealeaseList(): Observable<List<Release>>
}