package com.koresuniku.wishmaster_v4.application.shared_preferences

import android.content.Context
import com.koresuniku.wishmaster_v4.core.network.client.RetrofitHolder


interface ISharedPreferencesHelper {
    fun onApplicationCreate(context: Context,
                            sharedPreferencesStorage: ISharedPreferencesStorage,
                            retrofitHolder: RetrofitHolder,
                            sharedPreferencesUIDimens: ISharedPreferencesUiDimens)
}