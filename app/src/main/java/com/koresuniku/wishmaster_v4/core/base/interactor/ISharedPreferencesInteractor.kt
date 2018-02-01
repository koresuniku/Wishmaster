package com.koresuniku.wishmaster_v4.core.base.interactor

import com.koresuniku.wishmaster_v4.application.ISharedPreferencesStorage


interface ISharedPreferencesInteractor {
    fun getSharedPreferencesStorage(): ISharedPreferencesStorage
}