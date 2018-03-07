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

package com.koresuniku.wishmaster.ui.thread_list

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.AbsListView
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.application.utils.IntentKeystore
import com.koresuniku.wishmaster.core.module.thread_list.ThreadListContract
import com.koresuniku.wishmaster.application.global.WMTextUtils
import com.koresuniku.wishmaster.application.global.WMAnimationUtils
import com.koresuniku.wishmaster.ui.base.BaseWishmasterActivity
import com.koresuniku.wishmaster.ui.full_thread.FullThreadActivity
import com.koresuniku.wishmaster.ui.gallery.GalleryPagerAdapter
import com.koresuniku.wishmaster.ui.utils.UiUtils
import com.koresuniku.wishmaster.ui.view.recycler_view_fast_scroller.RecyclerFastScroller
import com.koresuniku.wishmaster.ui.view.widget.WishmasterRecyclerView
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayout
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection
import javax.inject.Inject

/**
 * Created by koresuniku on 01.01.18.
 */

class ThreadListActivity : BaseWishmasterActivity(), ThreadListContract.IThreadListMainView {
    private val LOG_TAG = ThreadListActivity::class.java.simpleName

    @Inject lateinit var presenter: ThreadListContract.IThreadListPresenter
    @Inject lateinit var textUtils: WMTextUtils
    @Inject lateinit var uiUtils: UiUtils
    @Inject lateinit var WMAnimationUtils: WMAnimationUtils

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
    @BindView(R.id.gallery_layout) lateinit var mGalleryLayout: ViewGroup
    @BindView(R.id.gallery_background) lateinit var mGalleryBackground: View
    @BindView(R.id.gallery_view_pager) lateinit var mGalleryViewPager: ViewPager

    private lateinit var mThreadListRecyclerViewAdapter: ThreadListRecyclerViewAdapter
    private lateinit var mGalleryPagerAdapter: GalleryPagerAdapter
    private var galleryOpenedState = false

    @SuppressLint("newApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        getWishmasterApplication().daggerThreadListViewComponent.inject(this)
        super.onCreate(savedInstanceState)

        ButterKnife.bind(this)
        uiUtils.showSystemUI(this)
        presenter.bindView(this)

        setupBackground()
        setupErrorLayout()
        setupToolbar()
        setupRefreshLayout()
        setupRecyclerView()
        setupViewPager()

        presenter.loadThreadList()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.thread_list_menu, menu)
        activityMenu = menu
        return super.onCreateOptionsMenu(menu)
    }

    override fun provideFromActivityRequestCode() = IntentKeystore.FROM_THREAD_LIST_ACTIVITY_REQUEST_CODE
    override fun getBoardId(): String = intent.getStringExtra(IntentKeystore.BOARD_ID_CODE)
    @LayoutRes override fun provideContentLayoutResource(): Int = R.layout.activity_thread_list

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
                    presenter.loadThreadList()
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

    private fun setupBackground() {
        if (BoardsBackgrounds.backgrounds.containsKey(getBoardId())) {
            mBackground.setImageResource(
                    BoardsBackgrounds.backgrounds[getBoardId()] ?: R.color.colorBackground)
        } else mBackground.setImageResource(R.color.colorBackground)
    }

    private fun setupErrorLayout() {
        mErrorLabel.text = getString(R.string.failed_to_load_threads)
    }

    private fun setupToolbar() {
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupTitle(boardName: String) {
        supportActionBar?.title = textUtils.obtainBoardIdDashName(getBoardId(), boardName)
    }

    private fun setupRefreshLayout() {
        mSwipyRefreshLayout.setDistanceToTriggerSync(100)
        mSwipyRefreshLayout.isEnabled = true
        mSwipyRefreshLayout.setOnRefreshListener { presenter.loadThreadList() }
    }

    private fun setupRecyclerView() {
        mRecyclerView.isVerticalScrollBarEnabled = false
        mThreadListRecyclerViewAdapter = ThreadListRecyclerViewAdapter(this)
        presenter.bindThreadListAdapterView(mThreadListRecyclerViewAdapter)
        mRecyclerView.setItemViewCacheSize(20)
        mRecyclerView.isDrawingCacheEnabled = true
        mRecyclerView.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.addItemDecoration(ThreadItemDividerDecoration(this))
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!isActivityDestroyed) {
                    if (newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                        Glide.with(this@ThreadListActivity).pauseRequests()
                    } else {
                        Glide.with(this@ThreadListActivity).resumeRequests()
                    }
                }
            }
        })
        mRecyclerView.setOnTouchListener { _, _ ->
            mRecyclerView.checkRefresh();false
        }
        mRecyclerView.adapter = mThreadListRecyclerViewAdapter
        mRecyclerView.attachAppBarLayout(mAppBarLayout)
        mRecyclerView.attachSwipyRefreshLayout(mSwipyRefreshLayout)
        mScroller.attachRecyclerView(mRecyclerView)
        mScroller.attachAdapter(mRecyclerView.adapter)
        mScroller.attachAppBarLayout(mCoordinator, mAppBarLayout)
    }

    private var yCoordinate = 0f
    private fun setupViewPager() {
//        mGalleryPagerAdapter = GalleryPagerAdapter(
//                supportFragmentManager, presenter, mediaTypeMatcher, retrofitHolder)
//        mGalleryViewPager.adapter = mGalleryPagerAdapter
//        mGalleryLayout.setOnTouchListener { view, motionEvent ->
//            when(motionEvent.action) {
//                MotionEvent.ACTION_DOWN -> {
//                    yCoordinate = view.y - motionEvent.rawY
//                }
//                MotionEvent.ACTION_MOVE -> {
//                    view.animate().y(motionEvent.rawY + yCoordinate).setDuration(0).start()
//                }
//                MotionEvent.ACTION_UP -> {
//                    //your stuff
//                }
//                else -> {}
//            }
//            true
//        }
    }

//    override fun openGallery() {
//        galleryOpenedState = true
//
//        mGalleryPagerAdapter.notifyDataSetChanged()
//        mGalleryViewPager.setCurrentItem(presenter.getGalleryState().currentPostPosition, false)
//        mGalleryLayout.visibility = View.VISIBLE
//
//        mGalleryBackground.alpha = 0f
//        mGalleryBackground.animate()
//                .alpha(1f)
//                .setDuration(resources.getInteger(R.integer.gallery_enter_duration).toLong())
//                .setInterpolator(LinearInterpolator())
//                .setListener(object : Animator.AnimatorListener {
//                    override fun onAnimationRepeat(p0: Animator?) {}
//                    override fun onAnimationEnd(p0: Animator?) {}
//                    override fun onAnimationCancel(p0: Animator?) {}
//                    override fun onAnimationStart(p0: Animator?) {
//                        uiUtils.setBarsTranslucent(this@ThreadListActivity, true)
//                    }
//                })
//                .start()
//    }
//
//    override fun closeGallery() {
//        mGalleryLayout.animate()
//                .alpha(0f)
//                .setDuration(resources.getInteger(R.integer.gallery_exit_duration).toLong())
//                .setInterpolator(LinearInterpolator())
//                .setListener(object : Animator.AnimatorListener {
//                    override fun onAnimationRepeat(p0: Animator?) {}
//
//                    override fun onAnimationEnd(p0: Animator?) {
//                        uiUtils.setBarsTranslucent(this@ThreadListActivity, false)
//                        mGalleryLayout.visibility = View.GONE
//                        mGalleryLayout.alpha = 1f
//                    }
//
//                    override fun onAnimationCancel(p0: Animator?) {}
//                    override fun onAnimationStart(p0: Animator?) {}
//                })
//                .start()
//
//        galleryOpenedState = false
//    }

    override fun onThreadListReceived(boardName: String) {
        hideLoading()
        setupTitle(boardName)
    }

    override fun showLoading() {
        activityMenu?.findItem(R.id.action_refresh)?.isEnabled = false
        mSwipyRefreshLayout.isEnabled = false
        supportActionBar?.title = getString(R.string.loading_text)
        if (!mSwipyRefreshLayout.isRefreshing)
            WMAnimationUtils.showLoadingYoba(mYobaImage, mLoadingLayout)
    }

    private fun hideLoading() {
        activityMenu?.findItem(R.id.action_refresh)?.isEnabled = true
        mSwipyRefreshLayout.isEnabled = true
        if (!mSwipyRefreshLayout.isRefreshing)
            WMAnimationUtils.hideLoadingYoba(mYobaImage, mLoadingLayout)
        if (mSwipyRefreshLayout.isRefreshing)
            mSwipyRefreshLayout.isRefreshing = false
        mRecyclerView.scrollToPosition(0)
        mAppBarLayout.setExpanded(true)
        mSwipyRefreshLayout.direction = SwipyRefreshLayoutDirection.TOP
        mSwipyRefreshLayout.isEnabled = true
    }

    override fun showError(message: String?) {
        hideLoading()

        mRecyclerView.visibility = View.GONE
        mErrorLayout.visibility = View.VISIBLE
        supportActionBar?.title = getString(R.string.error)
        val snackBar = Snackbar.make(
                mErrorLayout,
                message?:getString(R.string.error),
                Snackbar.LENGTH_INDEFINITE)
        snackBar.setAction(R.string.bljad, { snackBar.dismiss() })
        snackBar.show()
        mTryAgainButton.setOnClickListener { snackBar.dismiss(); hideError(); showLoading()
            presenter.loadThreadList()
            mRecyclerView.post { mRecyclerView.scheduleLayoutAnimation() } }
    }

    private fun hideError() {
        mErrorLayout.visibility = View.GONE
        mRecyclerView.visibility = View.VISIBLE
    }

    override fun launchFullThread(threadNumber: String) {
        val intent = Intent(this, FullThreadActivity::class.java)
        intent.putExtra(IntentKeystore.BOARD_ID_CODE, getBoardId())
        intent.putExtra(IntentKeystore.THREAD_NUMBER_CODE, threadNumber)
        startActivity(intent)
        overrideForwardPendingTransition()
    }

    //override fun galleryOpenedState() = galleryOpenedState

    override fun onBackPressed() {
        if (galleryOpenedState) //closeGallery()
        else {
            presenter.unbindThreadListAdapterView()
            super.onBackPressed()
            overrideBackwardPendingTransition()
        }
    }

    override fun onDestroy() {
        presenter.unbindThreadListAdapterView()
        super.onDestroy()
    }
}