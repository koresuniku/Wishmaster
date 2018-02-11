package com.koresuniku.wishmaster.core.base.interactor

import com.koresuniku.wishmaster.application.preferences.ISharedPreferencesStorage


interface ISharedPreferencesInteractor {
    fun getSharedPreferencesStorage(): ISharedPreferencesStorage
}