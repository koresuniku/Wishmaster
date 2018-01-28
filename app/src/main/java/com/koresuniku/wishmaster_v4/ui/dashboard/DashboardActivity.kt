package com.koresuniku.wishmaster_v4.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.design.widget.Snackbar
import android.support.design.widget.TabLayout
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import butterknife.BindView
import butterknife.ButterKnife

import com.koresuniku.wishmaster_v4.R
import com.koresuniku.wishmaster_v4.application.SharedPreferencesKeystore
import com.koresuniku.wishmaster_v4.application.SharedPreferencesStorage
import com.koresuniku.wishmaster_v4.core.dashboard.DashboardView
import com.koresuniku.wishmaster_v4.core.dashboard.DashboardPresenter
import com.koresuniku.wishmaster_v4.core.data.boards.BoardListData
import com.koresuniku.wishmaster_v4.ui.util.ViewUtils
import com.koresuniku.wishmaster_v4.ui.view.widget.DashboardViewPager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

import javax.inject.Inject
import android.support.v7.widget.SearchView
import com.koresuniku.wishmaster_v4.application.IntentKeystore
import com.koresuniku.wishmaster_v4.ui.base.BaseWishmasterActivity
import com.koresuniku.wishmaster_v4.ui.thread_list.ThreadListActivity

class DashboardActivity : BaseWishmasterActivity(), DashboardView {
    private val LOG_TAG = DashboardActivity::class.java.simpleName

    @Inject lateinit var presenter: DashboardPresenter
    @Inject lateinit var sharedPreferencesStorage: SharedPreferencesStorage

    @BindView(R.id.toolbar) lateinit var mToolbar: Toolbar
    @BindView(R.id.tab_layout) lateinit var mTabLayout: TabLayout
    @BindView(R.id.loading_layout) lateinit var mLoadingLayout: ViewGroup
    @BindView(R.id.error_layout) lateinit var mErrorLayout: ViewGroup
    @BindView(R.id.yoba) lateinit var mYobaImage: ImageView
    @BindView(R.id.dashboard_viewpager) lateinit var mViewPager: DashboardViewPager
    @BindView(R.id.try_again_button) lateinit var mTryAgainButton: Button

    private lateinit var mViewPagerAdapter: DashboardViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWishmasterApplication().getDashBoardComponent().inject(this)
        ButterKnife.bind(this)
        presenter.bindView(this)

        setSupportActionBar(mToolbar)
        setupViewPager()
        setupTabLayout()

        loadBoards()
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

    override fun onBackPressed() {
        super.onBackPressed()
        presenter.unbindView()
    }

    private fun loadBoards() {
        val loadBoardsObservable = presenter.getLoadBoardsObservable()
        mCompositeDisposable.add(loadBoardsObservable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( { boardListData: BoardListData ->
                    Log.d(LOG_TAG, "received board data: " + boardListData.getBoardList().size)
                    hideLoading()
                }, { throwable: Throwable ->
                    throwable.printStackTrace()
                    hideLoading()
                    showError(throwable)
                }))
    }

    @LayoutRes override fun provideContentLayoutResource(): Int = R.layout.activity_dashboard

    override fun showLoading() {
        runOnUiThread {
            mLoadingLayout.visibility = View.VISIBLE
            mViewPager.setPagingEnabled(false)
            val rotationAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_rotate_infinitely)
            mYobaImage.startAnimation(rotationAnimation)
        }
    }

    private fun hideLoading() {
        runOnUiThread {
            mViewPager.setPagingEnabled(true)
            ViewUtils.enableTabLayout(mTabLayout)
            mYobaImage.clearAnimation()
            mLoadingLayout.visibility = View.GONE
        }
    }

    private fun showError(throwable: Throwable) {
        runOnUiThread {
            mErrorLayout.visibility = View.VISIBLE
            mViewPager.setPagingEnabled(false)
            ViewUtils.disableTabLayout(mTabLayout)
            val snackbar = Snackbar.make(mErrorLayout, throwable.message.toString(), Snackbar.LENGTH_INDEFINITE)
            snackbar.setAction(R.string.bljad, { snackbar.dismiss() })
            snackbar.show()
            mTryAgainButton.setOnClickListener {
                snackbar.dismiss()
                hideError()
                showLoading()
                presenter.reloadBoards()
                loadBoards()
            }
        }
    }

    private fun hideError() {
        runOnUiThread { mErrorLayout.visibility = View.GONE }
    }

    private fun setupTabLayout() {
        mTabLayout.setupWithViewPager(mViewPager)

        mTabLayout.getTabAt(0)?.setIcon(R.drawable.ic_favorite_black_24dp)
        mTabLayout.getTabAt(1)?.setIcon(R.drawable.ic_format_list_bulleted_black_24dp)
        mTabLayout.getTabAt(2)?.setIcon(R.drawable.ic_star_black_24dp)
        mTabLayout.getTabAt(3)?.setIcon(R.drawable.ic_history_black_24dp)

        ViewUtils.disableTabLayout(mTabLayout)
    }

    private fun setupViewPager() {
        mViewPagerAdapter = DashboardViewPagerAdapter(supportFragmentManager)
        mViewPager.adapter = mViewPagerAdapter
        mViewPager.offscreenPageLimit = 2
        mCompositeDisposable.add(sharedPreferencesStorage.readInt(
                SharedPreferencesKeystore.DASHBOARD_PREFERRED_TAB_POSITION_KEY,
                SharedPreferencesKeystore.DASHBOARD_PREFERRED_TAB_POSITION_DEFAULT)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe { value -> mViewPager.currentItem = value })
    }

    override fun launchThreadListActivity(boardId: String) {
        Log.d(LOG_TAG, "received boardId: $boardId")
        val intent = Intent(this, ThreadListActivity::class.java)
        intent.putExtra(IntentKeystore.BOARD_ID_CODE, boardId)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
    }

    override fun showUnknownInput() {
        val snackbar = Snackbar.make(mErrorLayout, getString(R.string.unknown_address), Snackbar.LENGTH_INDEFINITE)
        snackbar.setAction(R.string.bljad, { snackbar.dismiss() })
        snackbar.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unbindView()
    }
}
