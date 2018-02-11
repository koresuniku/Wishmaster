package com.koresuniku.wishmaster.core.network.full_thread_api

import com.koresuniku.wishmaster.core.data.model.posts.Post
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query


interface FullThreadApiService {

    @GET("/makaba/mobile.fcgi")
    fun getPostListObservable(@Query("task") task: String,
                              @Query("board") board: String,
                              @Query("thread") thread: String,
                              @Query("post") post: Int): Observable<MutableList<Post>>
}