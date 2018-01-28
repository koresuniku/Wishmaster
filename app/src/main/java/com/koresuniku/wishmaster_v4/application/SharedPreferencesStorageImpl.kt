package com.koresuniku.wishmaster_v4.application

import android.content.Context
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by koresuniku on 29.11.17.
 */

class SharedPreferencesStorageImpl @Inject constructor(val context: Context) : SharedPreferencesStorage {

    override fun writeStringBackground(key: String, value: String) {
        context.getSharedPreferences(SharedPreferencesKeystore.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
                .edit()
                .putString(key, value)
                .apply()
    }

    override fun readString(key: String, defaultValue: String): Single<String> {
        return Single.fromCallable {
            context.getSharedPreferences(SharedPreferencesKeystore.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
                    .getString(key, defaultValue)
        }
    }

    override fun writeIntBackground(key: String, value: Int) {
        context.getSharedPreferences(SharedPreferencesKeystore.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
                .edit()
                .putInt(key, value)
                .apply()
    }

    override fun readInt(key: String, defaultValue: Int): Single<Int> {
        return Single.fromCallable {
            context.getSharedPreferences(SharedPreferencesKeystore.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
                    .getInt(key, defaultValue)
        }
    }

    override fun writeStringSameThread(key: String, value: String): Boolean {
        return context.getSharedPreferences(SharedPreferencesKeystore.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
                .edit()
                .putString(key, value)
                .commit()
    }

    override fun writeIntSameThread(key: String, value: Int): Boolean {
        return context.getSharedPreferences(SharedPreferencesKeystore.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
                .edit()
                .putInt(key, value)
                .commit()
    }
}