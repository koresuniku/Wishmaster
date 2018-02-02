package com.koresuniku.wishmaster_v4.core.base.interactor

import com.koresuniku.wishmaster_v4.application.shared_preferences.ISharedPreferencesStorage


interface ISharedPreferencesInteractor {
    fun getSharedPreferencesStorage(): ISharedPreferencesStorage
}