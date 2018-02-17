package com.koresuniku.wishmaster.core.modules.full_thread.view

import android.text.Spanned
import com.koresuniku.wishmaster.core.base.mvp.IMvpView

/**
 * Created by koresuniku on 2/11/18.
 */
interface FullThreadView<P> : IMvpView<P> {
    fun getBoardId(): String
    fun getThreadNumber(): String
    fun onPostListReceived(title: Spanned)
    fun showError(message: String?)
    fun showLoading()
}