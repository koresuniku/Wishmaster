package com.koresuniku.wishmaster.core.modules.full_thread

import android.text.Spanned
import com.koresuniku.wishmaster.core.base.mvp.IMvpView

/**
 * Created by koresuniku on 2/11/18.
 */
interface FullThreadView<P> : IMvpView<P> {
    fun getBoardId(): String
    fun getThreadNumber(): String
    fun onPostListReceived(title: Spanned, itemCount: Int)
    fun onNewPostsReceived(oldCount: Int, newCount: Int)
    fun showError(message: String?)
    fun showNewPostsError(message: String?)
    fun showLoading()
}