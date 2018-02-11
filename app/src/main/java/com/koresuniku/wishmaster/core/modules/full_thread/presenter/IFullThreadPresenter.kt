package com.koresuniku.wishmaster.core.modules.full_thread.presenter

import com.koresuniku.wishmaster.core.base.mvp.IMvpPresenter
import com.koresuniku.wishmaster.core.modules.full_thread.view.FullThreadView

/**
 * Created by koresuniku on 2/11/18.
 */
interface IFullThreadPresenter : IMvpPresenter<FullThreadView<IFullThreadPresenter>> {

    fun loadPostList()
    fun getBoardId(): String
    fun getThreadNumber(): String
    fun onNetworkError(t: Throwable)

}