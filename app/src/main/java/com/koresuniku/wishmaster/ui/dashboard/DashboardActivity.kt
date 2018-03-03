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

package com.koresuniku.wishmaster.ui.dashboard

import android.content.Intent
import android.content.pm.PackageManager
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
import com.koresuniku.wishmaster.core.modules.dashboard.DashboardView
import com.koresuniku.wishmaster.core.data.model.boards.BoardListData
import com.koresuniku.wishmaster.ui.utils.ViewUtils
import com.koresuniku.wishmaster.ui.view.widget.DashboardViewPager

import javax.inject.Inject
import android.support.v7.widget.SearchView
import com.koresuniku.wishmaster.application.utils.IntentKeystore
import com.koresuniku.wishmaster.core.modules.dashboard.IDashboardPresenter
import com.koresuniku.wishmaster.ui.base.BaseWishmasterActivity
import com.koresuniku.wishmaster.ui.thread_list.ThreadListActivity
import com.koresuniku.wishmaster.ui.utils.UiUtils
import android.view.MenuItem
import com.koresuniku.wishmaster.application.notifier.NewReleaseNotifier
import com.koresuniku.wishmaster.application.notifier.OnNewReleaseListener
import com.koresuniku.wishmaster.application.singletones.WMDownloadManager
import com.koresuniku.wishmaster.application.singletones.WMPermissionManager
import com.koresuniku.wishmaster.core.network.github_api.Asset
import com.koresuniku.wishmaster.ui.anim.WishmasterAnimationUtils
import android.support.v7.app.AlertDialog
import android.util.Log
import com.koresuniku.wishmaster.application.utils.FirebaseKeystore


class DashboardActivity : BaseWishmasterActivity<IDashboardPresenter>(),
        DashboardView<IDashboardPresenter>, OnNewReleaseListener {
    private val LOG_TAG = DashboardActivity::class.java.simpleName

    @Inject override lateinit var presenter: IDashboardPresenter
    @Inject lateinit var uiUtils: UiUtils
    @Inject lateinit var viewUtils: ViewUtils
    @Inject lateinit var wishmasterAnimationUtils: WishmasterAnimationUtils
    @Inject lateinit var newReleaseNotifier: NewReleaseNotifier
    @Inject lateinit var downloadManager: WMDownloadManager
    @Inject lateinit var permissionManager: WMPermissionManager

    @BindView(R.id.toolbar) lateinit var mToolbar: Toolbar
    @BindView(R.id.tab_layout) lateinit var mTabLayout: TabLayout
    @BindView(R.id.loading_layout) lateinit var mLoadingLayout: ViewGroup
    @BindView(R.id.error_layout) lateinit var mErrorLayout: ViewGroup
    @BindView(R.id.yoba) lateinit var mYobaImage: ImageView
    @BindView(R.id.dashboard_viewpager) lateinit var mViewPager: DashboardViewPager
    @BindView(R.id.try_again_button) lateinit var mTryAgainButton: Button

    private lateinit var mViewPagerAdapter: DashboardViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        getWishmasterApplication().daggerDashboardViewComponent.inject(this)
        super.onCreate(savedInstanceState)

        uiUtils.showSystemUI(this)

        ButterKnife.bind(this)
        presenter.bindView(this)
        newReleaseNotifier.bindListener(this)

        setSupportActionBar(mToolbar)
        setupViewPager()
        setupTabLayout()

        presenter.loadBoards()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.dashboard_menu, menu)
        activityMenu = menu

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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_download_new_version -> {
                newReleaseNotifier.cachedAsset?.let {
                    val builder = AlertDialog.Builder(this)
                    builder.setMessage(R.string.new_version_available)
                            .setPositiveButton(R.string.download_text, { dialog, id ->
                                if (permissionManager.checkAndRequestExternalStoragePermissionForLoadNewVersion(this)) {
                                    downloadManager.downloadWithNotification(it.downloadLink, it.name)
                                }
                            })
                            .setNegativeButton(R.string.cancel_text, { dialog, id -> })
                    builder.create().show()
                }
                return true
            }
            R.id.action_settings -> {
                launchSettingsActivity()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onNewRelease(asset: Asset) {
        activityMenu?.let { it.findItem(R.id.action_download_new_version).isVisible = true }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            WMPermissionManager.WRITE_EXTERNAL_STORAGE_FOR_LOAD_NEW_VERSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    newReleaseNotifier.cachedAsset?.let {
                        downloadManager.downloadWithNotification(it.downloadLink, it.name)
                    }
                }
                return
            }
        }
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
        startActivity(intent)
        overrideForwardPendingTransition()
    }

    override fun showUnknownInput() {
        val snackbar = Snackbar.make(mErrorLayout, getString(R.string.unknown_address), Snackbar.LENGTH_SHORT)
        snackbar.setAction(R.string.bljad, { snackbar.dismiss() })
        snackbar.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        newReleaseNotifier.unbindListener(this)
    }
}
