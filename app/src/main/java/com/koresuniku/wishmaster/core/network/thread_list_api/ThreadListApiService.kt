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

package com.koresuniku.wishmaster.core.network.thread_list_api

import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by koresuniku on 01.01.18.
 */

interface ThreadListApiService {

    @GET("/{id}/catalog.json")
    fun getThreadsCall(@Path("id") boardId: String): Call<ThreadListJsonSchemaCatalogResponse>

    @GET("/{id}/{page}.json")
    fun getThreadsByPageCall(@Path("id") boardId: String, @Path("page") page: String): Call<ThreadListJsonSchemaPageResponse>

    @GET("/{id}/catalog.json")
    fun getThreadsObservable(@Path("id") boardId: String): Observable<ThreadListJsonSchemaCatalogResponse>

    @GET("/{id}/{page}.json")
    fun getThreadsByPageObservable(@Path("id") boardId: String, @Path("page") page: String): Observable<ThreadListJsonSchemaCatalogResponse>
}