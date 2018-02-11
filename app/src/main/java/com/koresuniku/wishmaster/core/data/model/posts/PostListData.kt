package com.koresuniku.wishmaster.core.data.model.posts

import java.util.ArrayList

/**
 * Created by koresuniku on 2/11/18.
 */
class PostListData {

    lateinit var postList: MutableList<Post>

    companion object {
        fun emptyData(): PostListData {
            val data = PostListData()

            data.postList = ArrayList()

            return data
        }
    }
}