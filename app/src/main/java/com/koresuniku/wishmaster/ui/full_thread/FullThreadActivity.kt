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

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.annotation.LayoutRes
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.text.Spanned
import android.view.*
import android.widget.*
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.application.utils.IntentKeystore
import com.koresuniku.wishmaster.core.modules.full_thread.IFullThreadPresenter
import com.koresuniku.wishmaster.core.modules.full_thread.FullThreadView
import com.koresuniku.wishmaster.core.utils.text.WishmasterTextUtils
import com.koresuniku.wishmaster.ui.anim.WishmasterAnimationUtils
import com.koresuniku.wishmaster.ui.base.BaseWishmasterActivity
import com.koresuniku.wishmaster.ui.utils.UiUtils
import com.koresuniku.wishmaster.ui.view.recycler_view_fast_scroller.RecyclerFastScroller
import com.koresuniku.wishmaster.ui.view.widget.WishmasterRecyclerView
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayout
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection
import javax.inject.Inject

/**
 * Created by koresuniku on 2/11/18.
 */

class FullThreadActivity : BaseWishmasterActivity<IFullThreadPresenter>(), FullThreadView<IFullThreadPresenter> {
    private val LOG_TAG = FullThreadActivity::class.java.simpleName

    @Inject override lateinit var presenter: IFullThreadPresenter
    @Inject lateinit var textUtils: WishmasterTextUtils
    @Inject lateinit var uiUtils: UiUtils
    @Inject lateinit var wishmasterAnimationUtils: WishmasterAnimationUtils

    @BindView(R.id.coordinator) lateinit var mCoordinator: CoordinatorLayout
    @BindView(R.id.app_bar_layout) lateinit var mAppBarLayout: AppBarLayout
    @BindView(R.id.toolbar) lateinit var mToolbar: Toolbar
    @BindView(R.id.loading_layout) lateinit var mLoadingLayout: ViewGroup
    @BindView(R.id.yoba) lateinit var mYobaImage: ImageView
    @BindView(R.id.error_layout) lateinit var mErrorLayout: ViewGroup
    @BindView(R.id.failed_to_load_label) lateinit var mErrorLabel: TextView
    @BindView(R.id.try_again_button) lateinit var mTryAgainButton: Button
    @BindView(R.id.swipy_refresh_layout) lateinit var mSwipyRefreshLayout: SwipyRefreshLayout
    @BindView(R.id.recycler_view) lateinit var mRecyclerView: WishmasterRecyclerView
    @BindView(R.id.scroller) lateinit var mScroller: RecyclerFastScroller
    @BindView(R.id.background) lateinit var mBackground: ImageView

    private lateinit var mFullThreadRecyclerViewAdapter: FullThreadRecyclerViewAdapter

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWishmasterApplication().daggerFullThreadViewComponent.inject(this)
        uiUtils.showSystemUI(this)
        ButterKnife.bind(this)
        presenter.bindView(this)

        setupErrorLayout()
        setupToolbar()
        setupRefreshLayout()
        setupRecyclerView()

        presenter.loadPostList()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.full_thread_menu, menu)
        activityMenu = menu
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.action_refresh -> {
                if (mErrorLayout.visibility == View.VISIBLE) {
                    mTryAgainButton.performClick()
                } else {
                    mSwipyRefreshLayout.direction = SwipyRefreshLayoutDirection.TOP
                    mSwipyRefreshLayout.isRefreshing = true
                    presenter.loadNewPostList()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.action_refresh)?.isEnabled = false
        return super.onPrepareOptionsMenu(menu)
    }

    override fun provideFromActivityRequestCode() = IntentKeystore.FROM_FULL_THREAD_ACTIVITY_REQUEST_CODE
    @LayoutRes override fun provideContentLayoutResource() = R.layout.activity_full_thread

    private fun setupToolbar() {
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupErrorLayout() {
        mErrorLabel.text = getString(R.string.failed_to_load_posts)
    }

    private fun setupTitle(title:  Spanned) { supportActionBar?.title = title }

    private fun setupRefreshLayout() {
        mSwipyRefreshLayout.setDistanceToTriggerSync(100)
        mSwipyRefreshLayout.isEnabled = true
        mSwipyRefreshLayout.setOnRefreshListener { presenter.loadNewPostList() }
    }

    private fun setupRecyclerView() {
        wishmasterAnimationUtils.setSlideFromRightLayoutAnimation(mRecyclerView, this)
        mFullThreadRecyclerViewAdapter = FullThreadRecyclerViewAdapter(this)
        presenter.bindFullThreadAdapterView(mFullThreadRecyclerViewAdapter)
        mRecyclerView.setItemViewCacheSize(20)
        mRecyclerView.isDrawingCacheEnabled = true
        mRecyclerView.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!isActivityDestroyed) {
                    if (newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                        Glide.with(this@FullThreadActivity).pauseRequests()
                    } else {
                        Glide.with(this@FullThreadActivity).resumeRequests()
                    }
                }
            }
        })
        mRecyclerView.setOnTouchListener { _, _ -> mRecyclerView.checkRefresh(); false }
        mRecyclerView.adapter = mFullThreadRecyclerViewAdapter
        mRecyclerView.attachAppBarLayout(mAppBarLayout)
        mRecyclerView.attachSwipyRefreshLayout(mSwipyRefreshLayout)
        mScroller.attachRecyclerView(mRecyclerView)
        mScroller.attachAdapter(mRecyclerView.adapter)
        mScroller.attachAppBarLayout(mCoordinator, mAppBarLayout)
    }

    override fun onEnterAnimationComplete() {
        super.onEnterAnimationComplete()
        mRecyclerView.post {
            if (!presenter.isDataLoaded()) {
                mRecyclerView.scheduleLayoutAnimation()
            }
        }
    }

    override fun showLoading() {
        activityMenu?.findItem(R.id.action_refresh)?.isEnabled = false
        mSwipyRefreshLayout.isEnabled = false
        supportActionBar?.title = getString(R.string.loading_text)
        wishmasterAnimationUtils.showLoadingYoba(mYobaImage, mLoadingLayout)
    }

    override fun getBoardId() = intent.getStringExtra(IntentKeystore.BOARD_ID_CODE)
    override fun getThreadNumber() = intent.getStringExtra(IntentKeystore.THREAD_NUMBER_CODE)

    override fun onPostListReceived(title: Spanned, itemCount: Int) {
        hideLoading()
        setupTitle(title)
        mRecyclerView.isVerticalScrollBarEnabled =
                itemCount < RecyclerFastScroller.DEFAULT_POST_LIMIT_SHOWING_HANDLE
    }

    override fun onNewPostsReceived(oldCount: Int, newCount: Int) {
        hideLoading()
        if (oldCount == newCount) return

        Toast.makeText(
                this,
                textUtils.getNewPostsInfo(newCount - oldCount),
                Toast.LENGTH_SHORT)
                .show()

        if (!mRecyclerView.canScrollVertically(1)) {
            mRecyclerView.viewTreeObserver.addOnGlobalLayoutListener(
                    object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        mRecyclerView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                    if (newCount - oldCount < 10) {
                        mRecyclerView.smoothScrollToPosition(
                                mFullThreadRecyclerViewAdapter.itemCount - 1)
                    } else {
                        mRecyclerView.scrollToPosition(
                                mFullThreadRecyclerViewAdapter.itemCount - 1)
                    }
                }
            })
        }
    }

    private fun hideLoading() {
        activityMenu?.findItem(R.id.action_refresh)?.isEnabled = true
        mSwipyRefreshLayout.isEnabled = true
        if (!mSwipyRefreshLayout.isRefreshing)
            wishmasterAnimationUtils.hideLoadingYoba(mYobaImage, mLoadingLayout)
        if (mSwipyRefreshLayout.isRefreshing)
            mSwipyRefreshLayout.isRefreshing = false
    }

    override fun showError(message: String?) {
        hideLoading()

        mRecyclerView.visibility = View.GONE
        mErrorLayout.visibility = View.VISIBLE
        supportActionBar?.title = getString(R.string.error)
        val snackBar = Snackbar.make(
                mErrorLayout,
                message ?: getString(R.string.error),
                Snackbar.LENGTH_INDEFINITE)
        snackBar.setAction(R.string.bljad, { snackBar.dismiss() })
        snackBar.show()
        mTryAgainButton.setOnClickListener { snackBar.dismiss(); hideError(); showLoading()
            presenter.loadPostList()
            mRecyclerView.post { mRecyclerView.scheduleLayoutAnimation() }
        }
    }

    override fun showNewPostsError(message: String?) {
        val snackBar = Snackbar.make(
                mErrorLayout,
                message ?: getString(R.string.error),
                Snackbar.LENGTH_INDEFINITE)
        snackBar.setAction(R.string.bljad, { snackBar.dismiss() })
        snackBar.show()
    }

    private fun hideError() {
        mErrorLayout.visibility = View.GONE
        mRecyclerView.visibility = View.VISIBLE
    }

    override fun onBackPressed() {
        presenter.unbindFullThreadAdapterView()
        super.onBackPressed()
        overrideBackwardPendingTransition()
    }

    override fun onDestroy() {
        presenter.unbindFullThreadAdapterView()
        super.onDestroy()
    }
}