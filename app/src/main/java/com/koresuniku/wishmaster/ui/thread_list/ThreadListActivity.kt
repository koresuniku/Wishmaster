package com.koresuniku.wishmaster.ui.thread_list

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Button
import android.widget.ImageView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.application.IntentKeystore
import com.koresuniku.wishmaster.core.modules.thread_list.presenter.IThreadListPresenter
import com.koresuniku.wishmaster.core.modules.thread_list.view.ThreadListView
import com.koresuniku.wishmaster.core.utils.text.WishmasterTextUtils
import com.koresuniku.wishmaster.ui.anim.WishmasterAnimationUtils
import com.koresuniku.wishmaster.ui.base.BaseWishmasterActivity
import com.koresuniku.wishmaster.ui.full_thread.FullThreadActivity
import com.koresuniku.wishmaster.ui.utils.UiUtils
import com.koresuniku.wishmaster.ui.view.recycler_view_fast_scroller.RecyclerFastScroller
import com.koresuniku.wishmaster.ui.view.widget.WishmasterRecyclerView
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayout
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection
import javax.inject.Inject

/**
 * Created by koresuniku on 01.01.18.
 */

class ThreadListActivity : BaseWishmasterActivity<IThreadListPresenter>(), ThreadListView<IThreadListPresenter> {
    private val LOG_TAG = ThreadListActivity::class.java.simpleName

    @Inject override lateinit var presenter: IThreadListPresenter
    @Inject lateinit var textUtils: WishmasterTextUtils
    @Inject lateinit var uiUtils: UiUtils
    @Inject lateinit var wishmasterAnimationUtils: WishmasterAnimationUtils

    @BindView(R.id.coordinator) lateinit var mCoordinator: CoordinatorLayout
    @BindView(R.id.app_bar_layout) lateinit var mAppBarLayout: AppBarLayout
    @BindView(R.id.toolbar) lateinit var mToolbar: Toolbar
    @BindView(R.id.loading_layout) lateinit var mLoadingLayout: ViewGroup
    @BindView(R.id.yoba) lateinit var mYobaImage: ImageView
    @BindView(R.id.error_layout) lateinit var mErrorLayout: ViewGroup
    @BindView(R.id.try_again_button) lateinit var mTryAgainButton: Button
    @BindView(R.id.swipy_refresh_layout) lateinit var mSwipyRefreshLayout: SwipyRefreshLayout
    @BindView(R.id.thread_list) lateinit var mThreadListRecyclerView: WishmasterRecyclerView
    @BindView(R.id.scroller) lateinit var mScroller: RecyclerFastScroller
    @BindView(R.id.background) lateinit var mBackground: ImageView

    private lateinit var mThreadListRecyclerViewAdapter: ThreadListRecyclerViewAdapter

    @SuppressLint("newApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getWishmasterApplication().daggerThreadListViewComponent.inject(this)
        uiUtils.showSystemUI(this)
        ButterKnife.bind(this)
        presenter.bindView(this)

        wishmasterAnimationUtils.setThreadListTransitions(window, mToolbar, mThreadListRecyclerView)

        setupBackground()
        setupToolbar()
        setupRefreshLayout()
        setupRecyclerView()

        presenter.loadThreadList()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.thread_list_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun provideFromActivityRequestCode() = IntentKeystore.FROM_THREAD_LIST_ACTIVITY_REQUEST_CODE
    override fun getBoardId(): String = intent.getStringExtra(IntentKeystore.BOARD_ID_CODE)
    @LayoutRes override fun provideContentLayoutResource(): Int = R.layout.activity_thread_list

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            R.id.action_refresh -> {
                mSwipyRefreshLayout.direction = SwipyRefreshLayoutDirection.BOTH
                mSwipyRefreshLayout.isRefreshing = true
                presenter.loadThreadList()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun setupBackground() {
        if (BoardsBackgrounds.backgrounds.containsKey(getBoardId())) {
            mBackground.setImageResource(
                    BoardsBackgrounds.backgrounds[getBoardId()] ?: R.color.colorBackground)
        } else mBackground.setImageResource(R.color.colorBackground)
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
        wishmasterAnimationUtils.setSlideFromBottomLayoutAnimation(mThreadListRecyclerView)
        mThreadListRecyclerViewAdapter = ThreadListRecyclerViewAdapter(this)
        presenter.bindThreadListAdapterView(mThreadListRecyclerViewAdapter)
        mThreadListRecyclerView.setItemViewCacheSize(20)
        mThreadListRecyclerView.isDrawingCacheEnabled = true
        mThreadListRecyclerView.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
        mThreadListRecyclerView.layoutManager = LinearLayoutManager(this)
        mThreadListRecyclerView.addItemDecoration(ThreadItemDividerDecoration(this))
        mThreadListRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
        mThreadListRecyclerView.setOnTouchListener { view, motionEvent ->
            when (mThreadListRecyclerView.checkRefreshPossibility().possibility) {
                WishmasterRecyclerView.RefreshPossibility.TOP -> {
                    mSwipyRefreshLayout.direction = SwipyRefreshLayoutDirection.TOP
                    mSwipyRefreshLayout.isEnabled = true
                }
                WishmasterRecyclerView.RefreshPossibility.BOTTOM -> {
                    mSwipyRefreshLayout.direction = SwipyRefreshLayoutDirection.BOTTOM
                    mSwipyRefreshLayout.isEnabled = true
                }
                WishmasterRecyclerView.RefreshPossibility.BOTH -> {
                    mSwipyRefreshLayout.direction = SwipyRefreshLayoutDirection.BOTH
                    mSwipyRefreshLayout.isEnabled = true
                }
                else -> mSwipyRefreshLayout.isEnabled = false
            }
            false
        }
        mThreadListRecyclerView.adapter = mThreadListRecyclerViewAdapter

        mScroller.attachRecyclerView(mThreadListRecyclerView)
        mScroller.attachAdapter(mThreadListRecyclerView.adapter)
        mScroller.attachAppBarLayout(mCoordinator, mAppBarLayout)
    }

    override fun onEnterAnimationComplete() {
        super.onEnterAnimationComplete()
        mThreadListRecyclerView.post {
            if (!presenter.isDataLoaded()) mThreadListRecyclerView.scheduleLayoutAnimation()
        }
    }

    override fun onThreadListReceived(boardName: String) {
        hideLoading()
        setupTitle(boardName)
    }

    override fun showLoading() {
        supportActionBar?.title = getString(R.string.loading_text)
        if (!mSwipyRefreshLayout.isRefreshing)
            wishmasterAnimationUtils.showLoadingYoba(mYobaImage, mLoadingLayout)
    }

    private fun hideLoading() {
        if (!mSwipyRefreshLayout.isRefreshing)
            wishmasterAnimationUtils.hideLoadingYoba(mYobaImage, mLoadingLayout)
        if (mSwipyRefreshLayout.isRefreshing)
            mSwipyRefreshLayout.isRefreshing = false
        mThreadListRecyclerView.scrollToPosition(0)
        mAppBarLayout.setExpanded(true)
    }

    override fun showError(message: String?) {
        hideLoading()

        mThreadListRecyclerView.visibility = View.GONE
        mErrorLayout.visibility = View.VISIBLE
        supportActionBar?.title = getString(R.string.error)
        val snackBar = Snackbar.make(
                mErrorLayout,
                message?:getString(R.string.error),
                Snackbar.LENGTH_INDEFINITE)
        snackBar.setAction(R.string.bljad, { snackBar.dismiss() })
        snackBar.show()
        mTryAgainButton.setOnClickListener { snackBar.dismiss(); hideError(); showLoading()
            presenter.loadThreadList();
            mThreadListRecyclerView.post { mThreadListRecyclerView.scheduleLayoutAnimation() } }
    }

    private fun hideError() {
        mErrorLayout.visibility = View.GONE
        mThreadListRecyclerView.visibility = View.VISIBLE
    }

    override fun launchFullThreadActivity(threadNumber: String) {
        val intent = Intent(this, FullThreadActivity::class.java)
        intent.putExtra(IntentKeystore.BOARD_ID_CODE, getBoardId())
        intent.putExtra(IntentKeystore.THREAD_NUMBER_CODE, threadNumber)
        startActivity(intent)
        overrideForwardPendingTransition()
    }

    override fun onActivityReenter(resultCode: Int, data: Intent?) {
        super.onActivityReenter(resultCode, data)
        wishmasterAnimationUtils.slideFromLeft(mThreadListRecyclerView)
        wishmasterAnimationUtils.fadeToolbar(false, mToolbar)
    }

    override fun onBackPressed() {
        presenter.unbindThreadListAdapterView()
        super.onBackPressed()
        overrideBackwardPendingTransition()
    }

    override fun onDestroy() {
        presenter.unbindThreadListAdapterView()
        super.onDestroy()
    }
}