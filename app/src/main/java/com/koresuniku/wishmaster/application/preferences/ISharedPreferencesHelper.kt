package com.koresuniku.wishmaster.application.preferences

import android.content.Context
import com.koresuniku.wishmaster.application.singletones.CommonParams
import com.koresuniku.wishmaster.application.singletones.UiParams
import com.koresuniku.wishmaster.core.network.client.RetrofitHolder
import com.koresuniku.wishmaster.ui.utils.DeviceUtils
import com.koresuniku.wishmaster.ui.utils.UiUtils
import com.koresuniku.wishmaster.ui.utils.ViewUtils


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