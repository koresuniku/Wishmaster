package com.koresuniku.wishmaster.ui.full_thread

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.text.Spanned
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
import com.koresuniku.wishmaster.core.modules.full_thread.presenter.IFullThreadPresenter
import com.koresuniku.wishmaster.core.modules.full_thread.view.FullThreadView
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
    @BindView(R.id.try_again_button) lateinit var mTryAgainButton: Button
    @BindView(R.id.swipy_refresh_layout) lateinit var mSwipyRefreshLayout: SwipyRefreshLayout
    @BindView(R.id.post_list) lateinit var mFullThreadRecyclerView: WishmasterRecyclerView
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

        wishmasterAnimationUtils.setFullThreadTransitions(window, mToolbar, mFullThreadRecyclerView)

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
                mSwipyRefreshLayout.direction = SwipyRefreshLayoutDirection.BOTH
                mSwipyRefreshLayout.isRefreshing = true
                presenter.loadNewPostList()
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
        wishmasterAnimationUtils.fadeToolbar(false, mToolbar)
    }

    private fun setupTitle(title:  Spanned) { supportActionBar?.title = title }

    private fun setupRefreshLayout() {
        mSwipyRefreshLayout.setDistanceToTriggerSync(100)
        mSwipyRefreshLayout.isEnabled = true
        mSwipyRefreshLayout.setOnRefreshListener { presenter.loadNewPostList() }
    }

    private fun setupRecyclerView() {
        wishmasterAnimationUtils.setSlideFromRightLayoutAnimation(mFullThreadRecyclerView, this)
        mFullThreadRecyclerViewAdapter = FullThreadRecyclerViewAdapter(this)
        presenter.bindFullThreadAdapterView(mFullThreadRecyclerViewAdapter)
        mFullThreadRecyclerView.setItemViewCacheSize(20)
        mFullThreadRecyclerView.isDrawingCacheEnabled = true
        mFullThreadRecyclerView.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
        mFullThreadRecyclerView.layoutManager = LinearLayoutManager(this)
        mFullThreadRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
        mFullThreadRecyclerView.setOnTouchListener { view, motionEvent ->
            when (mFullThreadRecyclerView.checkRefreshPossibility().possibility) {
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
        mFullThreadRecyclerView.adapter = mFullThreadRecyclerViewAdapter
        mFullThreadRecyclerView.attachAppBarLayout(mAppBarLayout)
        mScroller.attachRecyclerView(mFullThreadRecyclerView)
        mScroller.attachAdapter(mFullThreadRecyclerView.adapter)
        mScroller.attachAppBarLayout(mCoordinator, mAppBarLayout)
    }

    override fun onEnterAnimationComplete() {
        super.onEnterAnimationComplete()
        mFullThreadRecyclerView.post {
            if (!presenter.isDataLoaded()) {
                mFullThreadRecyclerView.scheduleLayoutAnimation()
            }
        }
    }

    override fun showLoading() {
        activityMenu?.findItem(R.id.action_refresh)?.isEnabled = false
        supportActionBar?.title = getString(R.string.loading_text)
        wishmasterAnimationUtils.showLoadingYoba(mYobaImage, mLoadingLayout)
    }

    override fun getBoardId() = intent.getStringExtra(IntentKeystore.BOARD_ID_CODE)
    override fun getThreadNumber() = intent.getStringExtra(IntentKeystore.THREAD_NUMBER_CODE)

    override fun onPostListReceived(title: Spanned) {
        hideLoading()
        setupTitle(title)
    }

    private fun hideLoading() {
        activityMenu?.findItem(R.id.action_refresh)?.isEnabled = true
        if (!mSwipyRefreshLayout.isRefreshing)
            wishmasterAnimationUtils.hideLoadingYoba(mYobaImage, mLoadingLayout)
        if (mSwipyRefreshLayout.isRefreshing)
            mSwipyRefreshLayout.isRefreshing = false
    }

    override fun showError(message: String?) {
        hideLoading()

        mFullThreadRecyclerView.visibility = View.GONE
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
            mFullThreadRecyclerView.post { mFullThreadRecyclerView.scheduleLayoutAnimation() }
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
        mFullThreadRecyclerView.visibility = View.VISIBLE
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