package com.koresuniku.wishmaster_v4.ui.thread_list

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.koresuniku.wishmaster_v4.R
import com.koresuniku.wishmaster_v4.core.data.threads.ThreadListData
import com.koresuniku.wishmaster_v4.core.domain.client.RetrofitHolder
import com.koresuniku.wishmaster_v4.core.thread_list.ThreadListAdapterView
import com.koresuniku.wishmaster_v4.core.thread_list.ThreadListPresenter
import com.koresuniku.wishmaster_v4.ui.base.BaseWishmasterActivity
import retrofit2.Retrofit
import java.lang.ref.WeakReference
import javax.inject.Inject

/**
 * Created by koresuniku on 07.01.18.
 */

class ThreadListRecyclerViewAdapter() : RecyclerView.Adapter<ThreadItemViewHolder>(), ThreadListAdapterView {
    private val LOG_TAG = ThreadListRecyclerViewAdapter::class.java.simpleName

    @Inject lateinit var retrofitHolder: RetrofitHolder

    private lateinit var activity: WeakReference<Activity>
    private lateinit var presenter: ThreadListPresenter

    constructor(activity: BaseWishmasterActivity, presenter: ThreadListPresenter) : this() {
        activity.getWishmasterApplication().getThreadListComponent().inject(this)
        this.activity = WeakReference(activity)
        this.presenter = presenter
    }

    override fun onBindViewHolder(holder: ThreadItemViewHolder?, position: Int) {
        holder?.let { presenter.bindThreadItemViewByPosition(it, position) }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ThreadItemViewHolder {
        return when (viewType) {
            ThreadListPresenter.NO_IMAGES_CODE -> ThreadItemViewHolder(
                    LayoutInflater.from(parent?.context).inflate(
                            R.layout.thread_item_no_images, parent, false),
                    retrofitHolder.getBaseUrl())
            ThreadListPresenter.SINGLE_IMAGE_CODE -> ThreadItemViewHolder(
                    LayoutInflater.from(parent?.context).inflate(
                            R.layout.thread_item_single_image, parent, false),
                    retrofitHolder.getBaseUrl())
            ThreadListPresenter.MULTIPLE_IMAGES_CODE -> ThreadItemViewHolder(
                    LayoutInflater.from(parent?.context).inflate(
                            R.layout.thread_item_multiple_images, parent, false),
                    retrofitHolder.getBaseUrl())
            else -> ThreadItemViewHolder(View(parent?.context), String())
        }
    }

    override fun onThreadListDataChanged(newThreadListData: ThreadListData) {
        activity.get()?.runOnUiThread({ notifyDataSetChanged() })
        Log.d(LOG_TAG, "threadListData received")
    }

    override fun getItemViewType(position: Int): Int = presenter.getThreadItemType(position)
    override fun getItemCount(): Int = presenter.getThreadListDataSize()
    override fun getItemId(position: Int): Long = position.toLong()
}