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

package com.koresuniku.wishmaster.application.preferences

import io.reactivex.Single

/**
 * Created by koresuniku on 29.11.17.
 */

interface ISharedPreferencesStorage {
    fun writeStringBackground(key: String, value: String)
    fun writeStringSameThread(key: String, value: String): Boolean
    fun readString(key: String, defaultValue: String): Single<String>
    fun writeIntBackground(key: String, value: Int)
    fun writeIntSameThread(key: String, value: Int): Boolean
    fun readInt(key: String, defaultValue: Int): Single<Int>
    fun writeBoolean(key: String, value: Boolean)
    fun readBoolean(key: String, defaultValue: Boolean): Single<Boolean>
}