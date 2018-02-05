package com.koresuniku.wishmaster_v4.ui.thread_list

import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationUtils
import android.widget.AbsListView
import android.widget.Button
import android.widget.ImageView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.koresuniku.wishmaster_v4.R
import com.koresuniku.wishmaster_v4.application.IntentKeystore
import com.koresuniku.wishmaster_v4.core.thread_list.presenter.IThreadListPresenter
import com.koresuniku.wishmaster_v4.core.thread_list.view.ThreadListView
import com.koresuniku.wishmaster_v4.core.util.text.WishmasterTextUtils
import com.koresuniku.wishmaster_v4.ui.base.BaseWishmasterActivity
import com.koresuniku.wishmaster_v4.ui.view.widget.LinearLayoutManagerWrapper
import javax.inject.Inject

/**
 * Created by koresuniku on 01.01.18.
 */

class ThreadListActivity : BaseWishmasterActivity<IThreadListPresenter>(), ThreadListView<IThreadListPresenter> {
    private val LOG_TAG = ThreadListActivity::class.java.simpleName

    @Inject override lateinit var presenter: IThreadListPresenter
    @Inject lateinit var textUtils: WishmasterTextUtils

    @BindView(R.id.toolbar) lateinit var mToolbar: Toolbar
    @BindView(R.id.loading_layout) lateinit var mLoadingLayout: ViewGroup
    @BindView(R.id.yoba) lateinit var mYobaImage: ImageView
    @BindView(R.id.error_layout) lateinit var mErrorLayout: ViewGroup
    @BindView(R.id.try_again_button) lateinit var mTryAgainButton: Button
    @BindView(R.id.thread_list) lateinit var mThreadListRecyclerView: RecyclerView
    @BindView(R.id.background) lateinit var mBackground: ImageView

    private lateinit var mThreadListRecyclerViewAdapter: ThreadListRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWishmasterApplication().daggerThreadListViewComponent.inject(this)
        ButterKnife.bind(this)
        presenter.bindView(this)

        setupBackground()
        setupToolbar()
        setupRecyclerView()

        showLoading(true)
        presenter.loadThreadList()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        presenter.unbindView()
        presenter.unbindThreadListAdapterView()
        overridePendingTransition(R.anim.slide_in_back, R.anim.slide_out_back)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun getBoardId(): String = intent.getStringExtra(IntentKeystore.BOARD_ID_CODE)

    override fun provideContentLayoutResource(): Int = R.layout.activity_thread_list

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

    private fun setupRecyclerView() {
        mThreadListRecyclerViewAdapter = ThreadListRecyclerViewAdapter(this)
        mThreadListRecyclerView.adapter = mThreadListRecyclerViewAdapter
        mThreadListRecyclerView.layoutManager = LinearLayoutManagerWrapper(
                this, LinearLayoutManager.VERTICAL, false)
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
        presenter.bindThreadListAdapterView(mThreadListRecyclerViewAdapter)
    }


    override fun onThreadListReceived(boardName: String) {
        hideLoading()
        setupTitle(boardName)
    }

    private fun showLoading(delay: Boolean) {
        mLoadingLayout.visibility = View.VISIBLE
        supportActionBar?.title = getString(R.string.loading_text)
        mYobaImage.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        val rotationAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_rotate_infinitely)
        Handler().postDelayed(
                { mYobaImage.startAnimation(rotationAnimation) },
                if (delay) resources.getInteger(R.integer.slide_anim_duration).toLong() else 0)
    }

    private fun hideLoading() {
        mYobaImage.clearAnimation()
        mYobaImage.setLayerType(View.LAYER_TYPE_NONE, null)
        mLoadingLayout.visibility = View.GONE
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
        mTryAgainButton.setOnClickListener {
            snackBar.dismiss()
            hideError()
            showLoading(false)
            presenter.loadThreadList()
        }
    }

    private fun hideError() {
        mErrorLayout.visibility = View.GONE
        mThreadListRecyclerView.visibility = View.VISIBLE
    }

    override fun showThreadList() {
        val alpha = AlphaAnimation(0f, 1f)
        alpha.duration = resources.getInteger(R.integer.showing_list_duration).toLong()
        mThreadListRecyclerView.startAnimation(alpha)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unbindThreadListAdapterView()
    }
}