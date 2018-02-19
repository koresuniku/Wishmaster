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

import android.content.Context
import android.preference.PreferenceManager
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by koresuniku on 29.11.17.
 */

class SharedPreferencesStorage @Inject constructor(val context: Context) : ISharedPreferencesStorage {

    override fun writeStringBackground(key: String, value: String) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(key, value)
                .apply()
    }

    override fun readString(key: String, defaultValue: String): Single<String> {
        return Single.fromCallable {
            PreferenceManager.getDefaultSharedPreferences(context)
                    .getString(key, defaultValue)
        }
    }

    override fun writeIntBackground(key: String, value: Int) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putInt(key, value)
                .apply()
    }

    override fun readInt(key: String, defaultValue: Int): Single<Int> {
        return Single.fromCallable {
            PreferenceManager.getDefaultSharedPreferences(context)
                    .getInt(key, defaultValue)
        }
    }

    override fun writeStringSameThread(key: String, value: String): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(key, value)
                .commit()
    }

    override fun writeIntSameThread(key: String, value: Int): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putInt(key, value)
                .commit()
    }

    override fun writeBoolean(key: String, value: Boolean) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(key, value)
                .apply()
    }

    override fun readBoolean(key: String, defaultValue: Boolean): Single<Boolean> {
        return Single.fromCallable {
            PreferenceManager.getDefaultSharedPreferences(context)
                    .getBoolean(key, defaultValue)
        }
    }
}