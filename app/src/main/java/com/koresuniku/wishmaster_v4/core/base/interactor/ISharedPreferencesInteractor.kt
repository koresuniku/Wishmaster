package com.koresuniku.wishmaster_v4.core.base.interactor

import com.koresuniku.wishmaster_v4.application.preferences.ISharedPreferencesStorage


interface ISharedPreferencesInteractor {
    fun getSharedPreferencesStorage(): ISharedPreferencesStorage
}