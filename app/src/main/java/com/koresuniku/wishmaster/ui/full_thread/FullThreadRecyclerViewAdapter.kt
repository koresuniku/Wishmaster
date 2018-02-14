package com.koresuniku.wishmaster.ui.full_thread

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.koresuniku.wishmaster.core.dagger.IWishmasterDaggerInjector
import com.koresuniku.wishmaster.core.data.model.posts.PostListData
import com.koresuniku.wishmaster.core.modules.full_thread.presenter.IFullThreadPresenter
import com.koresuniku.wishmaster.core.modules.full_thread.view.FullThreadAdapterView
import com.koresuniku.wishmaster.ui.base.BaseWishmasterActivity
import java.lang.ref.WeakReference
import javax.inject.Inject

/**
 * Created by koresuniku on 2/14/18.
 */

class FullThreadRecyclerViewAdapter() : RecyclerView.Adapter<PostItemViewHolder>(),
        FullThreadAdapterView<IFullThreadPresenter> {
    private val LOG_TAG = FullThreadRecyclerViewAdapter::class.java.simpleName

    override val NO_IMAGES_CODE = 0
    override val SINGLE_IMAGE_CODE = 1
    override val MULTIPLE_IMAGES_CODE = 2

    private lateinit var activity: WeakReference<Activity>
    @Inject override lateinit var presenter: IFullThreadPresenter
    @Inject lateinit var injector: IWishmasterDaggerInjector

    constructor(activity: BaseWishmasterActivity<IFullThreadPresenter>) : this() {
        activity.getWishmasterApplication().daggerFullThreadViewComponent.inject(this)
        this.activity = WeakReference(activity)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PostItemViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: PostItemViewHolder?, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPostListDataChanged(newPostListData: PostListData) {

    }

    override fun onOrientationChanged(orientation: Int) {

    }

    override fun getItemViewType(position: Int): Int = presenter.getPostItemType(position)
    override fun getItemCount(): Int = presenter.getDataSize()
    override fun getItemId(position: Int): Long = position.toLong()
}