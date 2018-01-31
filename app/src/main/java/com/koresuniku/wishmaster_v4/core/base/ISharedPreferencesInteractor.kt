package com.koresuniku.wishmaster_v4.core.base

import com.koresuniku.wishmaster_v4.application.ISharedPreferencesStorage


interface ISharedPreferencesInteractor {
    fun getSharedPreferencesStorage(): ISharedPreferencesStorage
}