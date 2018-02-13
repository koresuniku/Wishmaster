package com.koresuniku.wishmaster.ui.dashboard

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.design.widget.Snackbar
import android.support.design.widget.TabLayout
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import butterknife.BindView
import butterknife.ButterKnife

import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.core.modules.dashboard.view.DashboardView
import com.koresuniku.wishmaster.core.data.model.boards.BoardListData
import com.koresuniku.wishmaster.ui.utils.ViewUtils
import com.koresuniku.wishmaster.ui.view.widget.DashboardViewPager

import javax.inject.Inject
import android.support.v7.widget.SearchView
import com.koresuniku.wishmaster.application.IntentKeystore
import com.koresuniku.wishmaster.core.modules.dashboard.presenter.IDashboardPresenter
import com.koresuniku.wishmaster.ui.base.BaseWishmasterActivity
import com.koresuniku.wishmaster.ui.thread_list.ThreadListActivity
import com.koresuniku.wishmaster.ui.utils.UiUtils
import android.support.v4.app.ActivityOptionsCompat
import android.util.Log
import com.koresuniku.wishmaster.ui.anim.WishmasterAnimationUtils

class DashboardActivity : BaseWishmasterActivity<IDashboardPresenter>(), DashboardView<IDashboardPresenter> {


    private val LOG_TAG = DashboardActivity::class.java.simpleName

    @Inject override lateinit var presenter: IDashboardPresenter
    @Inject lateinit var uiUtils: UiUtils
    @Inject lateinit var viewUtils: ViewUtils
    @Inject lateinit var wishmasterAnimationUtils: WishmasterAnimationUtils

    @BindView(R.id.toolbar) lateinit var mToolbar: Toolbar
    @BindView(R.id.tab_layout) lateinit var mTabLayout: TabLayout
    @BindView(R.id.loading_layout) lateinit var mLoadingLayout: ViewGroup
    @BindView(R.id.error_layout) lateinit var mErrorLayout: ViewGroup
    @BindView(R.id.yoba) lateinit var mYobaImage: ImageView
    @BindView(R.id.dashboard_viewpager) lateinit var mViewPager: DashboardViewPager
    @BindView(R.id.try_again_button) lateinit var mTryAgainButton: Button

    private lateinit var mViewPagerAdapter: DashboardViewPagerAdapter

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getWishmasterApplication().daggerDashboardViewComponent.inject(this)
        uiUtils.showSystemUI(this)
        ButterKnife.bind(this)
        wishmasterAnimationUtils.setDashboardTransitions(window, mToolbar, mTabLayout)
        presenter.bindView(this)

        setSupportActionBar(mToolbar)
        setupViewPager()
        setupTabLayout()

        presenter.loadBoards()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.dashboard_menu, menu)

        val searchViewMenuItem = menu?.findItem(R.id.action_search)
        val searchView = searchViewMenuItem?.actionView as SearchView
        searchView.maxWidth = Int.MAX_VALUE
        searchView.queryHint = getString(R.string.dashboard_search_hint)
        searchView.setOnQueryTextFocusChangeListener { v, hasFocus ->
            if (!hasFocus) searchViewMenuItem.collapseActionView()
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchViewMenuItem.collapseActionView()
                searchView.postDelayed( {query?.let { presenter.processSearchInput(it) } }, 100)
                return false
            }
            override fun onQueryTextChange(newText: String?) = false
        })

        return super.onCreateOptionsMenu(menu)
    }

    @LayoutRes override fun provideContentLayoutResource() = R.layout.activity_dashboard
    override fun onBoardListReceived(boardListData: BoardListData) { hideLoading() }
    override fun onBoardListError(t: Throwable) { hideLoading(); showError(t) }
    override fun provideFromActivityRequestCode() = IntentKeystore.FROM_DASHBOARD_ACTIVITY_REQUEST_CODE

    override fun showLoading() {
        wishmasterAnimationUtils.showLoadingYoba(mYobaImage, mLoadingLayout)
        mViewPager.setPagingEnabled(false)
    }

    private fun hideLoading() {
        mViewPager.setPagingEnabled(true)
        viewUtils.enableTabLayout(mTabLayout)
        mLoadingLayout.visibility = View.GONE
        mYobaImage.clearAnimation()
    }

    private fun showError(throwable: Throwable) {
        mErrorLayout.visibility = View.VISIBLE
        mViewPager.setPagingEnabled(false)
        viewUtils.disableTabLayout(mTabLayout)
        val snackbar = Snackbar.make(mErrorLayout, throwable.message.toString(), Snackbar.LENGTH_INDEFINITE)
        snackbar.setAction(R.string.bljad, { snackbar.dismiss() })
        snackbar.show()
        mTryAgainButton.setOnClickListener {
            snackbar.dismiss(); hideError(); showLoading(); presenter.loadBoards()
        }
    }

    private fun hideError() { mErrorLayout.visibility = View.GONE }

    private fun setupTabLayout() {
        mTabLayout.setupWithViewPager(mViewPager)

        mTabLayout.getTabAt(0)?.setIcon(R.drawable.ic_favorite_black_24dp)
        mTabLayout.getTabAt(1)?.setIcon(R.drawable.ic_format_list_bulleted_black_24dp)
        mTabLayout.getTabAt(2)?.setIcon(R.drawable.ic_star_black_24dp)
        mTabLayout.getTabAt(3)?.setIcon(R.drawable.ic_history_black_24dp)

        viewUtils.disableTabLayout(mTabLayout)
    }

    private fun setupViewPager() {
        mViewPagerAdapter = DashboardViewPagerAdapter(supportFragmentManager)
        mViewPager.adapter = mViewPagerAdapter
        mViewPager.offscreenPageLimit = 2
        presenter.getDashboardFavouriteTabPosition()
    }

    override fun onFavouriteTabPositionReceived(position: Int) {
        mViewPager.currentItem = position
    }

    override fun launchThreadListActivity(boardId: String) {
        val intent = Intent(this, ThreadListActivity::class.java)
        intent.putExtra(IntentKeystore.BOARD_ID_CODE, boardId)
        launchNextActivityWithtransition(intent)
    }

    override fun showUnknownInput() {
        val snackbar = Snackbar.make(mErrorLayout, getString(R.string.unknown_address), Snackbar.LENGTH_SHORT)
        snackbar.setAction(R.string.bljad, { snackbar.dismiss() })
        snackbar.show()
    }
}
