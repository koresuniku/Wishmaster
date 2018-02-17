package com.koresuniku.wishmaster.core.modules.full_thread.presenter

import com.koresuniku.wishmaster.core.base.mvp.IMvpDataPresenter
import com.koresuniku.wishmaster.core.base.mvp.IMvpPresenter
import com.koresuniku.wishmaster.core.data.model.posts.PostListData
import com.koresuniku.wishmaster.core.modules.full_thread.view.FullThreadAdapterView
import com.koresuniku.wishmaster.core.modules.full_thread.view.FullThreadView
import com.koresuniku.wishmaster.core.modules.full_thread.view.PostItemView

/**
 * Created by koresuniku on 2/11/18.
 */
interface IFullThreadPresenter : IMvpPresenter<FullThreadView<IFullThreadPresenter>>,
        IMvpDataPresenter<PostListData> {
    var fullThreadAdapterView: FullThreadAdapterView<IFullThreadPresenter>?

    fun loadPostList()
    fun loadNewPostList()
    fun getBoardId(): String
    fun getThreadNumber(): String
    fun onNetworkError(t: Throwable)
    fun onNetworkNewPostsError(t: Throwable)
    fun getPostItemType(position: Int): Int
    fun setItemViewData(postItemView: PostItemView, position: Int)

    fun bindFullThreadAdapterView(fullThreadAdapterView: FullThreadAdapterView<IFullThreadPresenter>)
    fun unbindFullThreadAdapterView()

}