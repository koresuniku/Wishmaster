package com.koresuniku.wishmaster.core.modules.full_thread.view

import com.koresuniku.wishmaster.application.listener.OnOrientationChangedListener
import com.koresuniku.wishmaster.core.base.mvp.IMvpView
import com.koresuniku.wishmaster.core.data.model.posts.PostListData

/**
 * Created by koresuniku on 2/14/18.
 */

interface FullThreadAdapterView<P> : IMvpView<P>, OnOrientationChangedListener {
    val NO_IMAGES_CODE: Int
    val SINGLE_IMAGE_CODE: Int
    val MULTIPLE_IMAGES_CODE: Int
    fun onPostListDataChanged(newPostListData: PostListData)
}