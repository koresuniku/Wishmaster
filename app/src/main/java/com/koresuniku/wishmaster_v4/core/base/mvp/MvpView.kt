package com.koresuniku.wishmaster_v4.core.base.mvp

import android.app.Application
import android.content.Context
import android.support.v7.view.menu.MenuView
import com.koresuniku.wishmaster_v4.application.WishmasterApplication

/**
 * Created by koresuniku on 03.10.17.
 */

interface MvpView<P> {
    var presenter: P
}