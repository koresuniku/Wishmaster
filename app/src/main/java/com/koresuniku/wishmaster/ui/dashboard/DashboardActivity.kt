package com.koresuniku.wishmaster.ui.dashboard

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.annotation.RequiresApi
import android.support.design.widget.Snackbar
import android.support.design.widget.TabLayout
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
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
import android.transition.Explode
import com.koresuniku.wishmaster.application.IntentKeystore
import com.koresuniku.wishmaster.core.modules.dashboard.presenter.IDashboardPresenter
import com.koresuniku.wishmaster.ui.base.BaseWishmasterActivity
import com.koresuniku.wishmaster.ui.thread_list.ThreadListActivity
import com.koresuniku.wishmaster.ui.utils.UiUtils
import android.support.v4.app.ActivityOptionsCompat



class DashboardActivity : BaseWishmasterActivity<IDashboardPresenter>(), DashboardView<IDashboardPresenter> {
    private val LOG_TAG = DashboardActivity::class.java.simpleName

    @Inject override lateinit var presenter: IDashboardPresenter
    @Inject lateinit var uiUtils: UiUtils
    @Inject lateinit var viewUtils: ViewUtils

    @BindView(R.id.toolbar) lateinit var mToolbar: Toolbar
    @BindView(R.id.tab_layout) lateinit var mTabLayout: TabLayout
    @BindView(R.id.loading_layout) lateinit var mLoadingLayout: ViewGroup
    @BindView(R.id.error_layout) lateinit var mErrorLayout: ViewGroup
    @BindView(R.id.yoba) lateinit var mYobaImage: ImageView
    @BindView(R.id.dashboard_viewpager) lateinit var mViewPager: DashboardViewPager
    @BindView(R.id.try_again_button) lateinit var mTryAgainButton: Button

    private lateinit var mViewPagerAdapter: DashboardViewPagerAdapter

@SuppressLint("newApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getWishmasterApplication().daggerDashboardViewComponent.inject(this)
        uiUtils.showSystemUI(this)
        ButterKnife.bind(this)
        presenter.bindView(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val explode = Explode()
            explode.duration = 3000
            explode.excludeTarget(mToolbar, true)
            window.exitTransition = explode
        }

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
                query?.let { presenter.processSearchInput(it) }
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean = false
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onBoardListReceived(boardListData: BoardListData) { hideLoading() }
    override fun onBoardListError(t: Throwable) { hideLoading(); showError(t) }

    @LayoutRes override fun provideContentLayoutResource(): Int = R.layout.activity_dashboard

    override fun showLoading() {
        mLoadingLayout.visibility = View.VISIBLE
        mViewPager.setPagingEnabled(false)
        val rotationAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_rotate_infinitely)
        mYobaImage.startAnimation(rotationAnimation)
    }

    private fun hideLoading() {
        mViewPager.setPagingEnabled(true)
        viewUtils.enableTabLayout(mTabLayout)
        mYobaImage.clearAnimation()
        mLoadingLayout.visibility = View.GONE
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
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            startActivity(intent, options.toBundle())
        } else startActivity(intent)
        //overridePendingTransitionEnter()
    }

    override fun showUnknownInput() {
        val snackbar = Snackbar.make(mErrorLayout, getString(R.string.unknown_address), Snackbar.LENGTH_SHORT)
        snackbar.setAction(R.string.bljad, { snackbar.dismiss() })
        snackbar.show()
    }
}
