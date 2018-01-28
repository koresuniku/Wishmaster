package com.koresuniku.wishmaster_v4.core.base

import com.koresuniku.wishmaster_v4.application.WishmasterApplication

/**
 * Created by koresuniku on 26.12.17.
 */

interface IActivityMvpView : IMvpView {
    fun getWishmasterApplication(): WishmasterApplication
}