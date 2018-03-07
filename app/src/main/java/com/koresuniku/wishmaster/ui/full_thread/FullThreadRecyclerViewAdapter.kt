/*
 * Copyright (c) 2018 koresuniku
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.koresuniku.wishmaster.ui.full_thread

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.core.dagger.IWishmasterDaggerInjector
import com.koresuniku.wishmaster.core.data.model.posts.PostListData
import com.koresuniku.wishmaster.core.module.full_thread.FullThreadContract
import com.koresuniku.wishmaster.ui.base.BaseWishmasterActivity
import java.lang.ref.WeakReference
import javax.inject.Inject

/**
 * Created by koresuniku on 2/14/18.
 */

class FullThreadRecyclerViewAdapter() : RecyclerView.Adapter<PostItemViewHolder>(),
        FullThreadContract.IFullThreadAdapterView {

    private val LOG_TAG = FullThreadRecyclerViewAdapter::class.java.simpleName

    override val NO_IMAGES_CODE = 0
    override val SINGLE_IMAGE_CODE = 1
    override val MULTIPLE_IMAGES_CODE = 2

    private lateinit var activity: WeakReference<Activity>
    @Inject lateinit var presenter: FullThreadContract.IFullThreadPresenter
    @Inject lateinit var injector: IWishmasterDaggerInjector

    constructor(activity: BaseWishmasterActivity) : this() {
        activity.getWishmasterApplication().daggerFullThreadViewComponent.inject(this)
        this.activity = WeakReference(activity)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PostItemViewHolder {
        return when (viewType) {
            NO_IMAGES_CODE -> PostItemViewHolder(
                    LayoutInflater.from(parent?.context).inflate(
                            R.layout.post_item_no_images, parent, false), injector)
            SINGLE_IMAGE_CODE -> PostItemViewHolder(
                    LayoutInflater.from(parent?.context).inflate(
                            R.layout.post_item_single_image, parent, false), injector)
            MULTIPLE_IMAGES_CODE -> PostItemViewHolder(
                    LayoutInflater.from(parent?.context).inflate(
                            R.layout.post_item_multiple_images, parent, false), injector)
            else -> PostItemViewHolder(View(parent?.context), injector)
        }
    }

    override fun onBindViewHolder(holder: PostItemViewHolder?, position: Int) {
        holder?.let {
            presenter.setItemViewData(it, position)
        }
    }

    override fun onPostListDataChanged(newPostListData: PostListData) {
        activity.get()?.runOnUiThread { notifyDataSetChanged() }
    }

    override fun onNewPostsReceived(oldCount: Int, newCount: Int) {
        activity.get()?.runOnUiThread {
            notifyItemRangeInserted(oldCount , newCount - oldCount)
        }
    }

    override fun onOrientationChanged(orientation: Int) { notifyDataSetChanged() }
    override fun getItemViewType(position: Int): Int = presenter.getPostItemType(position)
    override fun getItemCount(): Int = presenter.getDataSize()
    override fun getItemId(position: Int): Long = position.toLong()
}