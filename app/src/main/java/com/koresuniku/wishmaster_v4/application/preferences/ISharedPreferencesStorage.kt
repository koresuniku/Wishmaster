package com.koresuniku.wishmaster_v4.application.preferences

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

}