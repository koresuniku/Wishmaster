package com.koresuniku.wishmaster_v4.application.preferences

import android.content.Context
import com.koresuniku.wishmaster_v4.core.network.client.RetrofitHolder
import com.koresuniku.wishmaster_v4.ui.utils.DeviceUtils
import com.koresuniku.wishmaster_v4.ui.utils.UiUtils
import com.koresuniku.wishmaster_v4.ui.utils.ViewUtils


interface ISharedPreferencesHelper {
    fun onApplicationCreate(context: Context,
                            sharedPreferencesStorage: SharedPreferencesStorage,
                            retrofitHolder: RetrofitHolder,
                            uiParams: UiParams,
                            commonParams: CommonParams,
                            uiUtils: UiUtils,
                            viewUtils: ViewUtils,
                            deviceUtils: DeviceUtils)
}