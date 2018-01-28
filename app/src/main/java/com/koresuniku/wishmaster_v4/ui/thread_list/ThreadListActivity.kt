package com.koresuniku.wishmaster_v4.ui.thread_list

import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.AbsListView
import android.widget.Button
import android.widget.ImageView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.koresuniku.wishmaster_v4.R
import com.koresuniku.wishmaster_v4.application.IntentKeystore
import com.koresuniku.wishmaster_v4.application.SharedPreferencesStorage
import com.koresuniku.wishmaster_v4.core.thread_list.ThreadListPresenter
import com.koresuniku.wishmaster_v4.core.thread_list.ThreadListView
import com.koresuniku.wishmaster_v4.core.util.text.WishmasterTextUtils
import com.koresuniku.wishmaster_v4.ui.base.BaseWishmasterActivity
import com.koresuniku.wishmaster_v4.ui.view.widget.LinearLayoutManagerWrapper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by koresuniku on 01.01.18.
 */

class ThreadListActivity : BaseWishmasterActivity(), ThreadListView {
    private val LOG_TAG = ThreadListActivity::class.java.simpleName

    @Inject lateinit var presenter: ThreadListPresenter
    @Inject lateinit var sharedPreferencesStorage: SharedPreferencesStorage

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
        getWishmasterApplication().getThreadListComponent().inject(this)
        ButterKnife.bind(this)
        presenter.bindView(this)

        setupBackground()
        setupToolbar()
        setupRecyclerView()

        showLoading(true)
        loadThreads(true)
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
            BoardsBackgrounds.backgrounds[getBoardId()]?.let { mBackground.setImageResource(it) }
        } else mBackground.setImageResource(R.color.colorBackground)
    }

    private fun setupToolbar() {
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupTitle(boardName: String) {
        supportActionBar?.title = WishmasterTextUtils.obtainBoardIdDashName(getBoardId(), boardName)
    }

    private fun setupRecyclerView() {
        mThreadListRecyclerViewAdapter = ThreadListRecyclerViewAdapter(this, presenter)
        mThreadListRecyclerView.adapter = mThreadListRecyclerViewAdapter
        mThreadListRecyclerView.layoutManager = LinearLayoutManagerWrapper(
                this, LinearLayoutManager.VERTICAL, false)
        mThreadListRecyclerView.addItemDecoration(ThreadItemDividerDecoration(this))
//        mThreadListRecyclerView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
//            override fun onGlobalLayout() {
//                mThreadListRecyclerView.viewTreeObserver.removeOnGlobalLayoutListener(this)
//                mThreadListRecyclerView.scrollToPosition(0)
//            }
//        })
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
//        mThreadListRecyclerView.setItemViewCacheSize(1024)
//        mThreadListRecyclerView.isDrawingCacheEnabled = true
//        mThreadListRecyclerView.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
        presenter.bindThreadListAdapterView(mThreadListRecyclerViewAdapter)
    }

    private fun loadThreads(first: Boolean) {
        presenter.reloadThreads()
        mCompositeDisposable.add(presenter.getLoadThreadsSingle()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { threadListData ->
                            Log.d(LOG_TAG, "data size: ${threadListData.getThreadList().size}")
                            //Log.d(LOG_TAG, "first thread: ${threadListData.getThreadList()[0]}")
                            hideLoading()
                            setupTitle(threadListData.getBoardName())
                            if (first) showThreadList()
                        }, { e -> e.printStackTrace(); hideLoading(); showError(e) })
        )
    }

    private fun showLoading(delay: Boolean) {
        runOnUiThread {
            mLoadingLayout.visibility = View.VISIBLE
            supportActionBar?.title = getString(R.string.loading_text)
            mYobaImage.setLayerType(View.LAYER_TYPE_HARDWARE, null)
            val rotationAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_rotate_infinitely)
            Handler().postDelayed(
                    { mYobaImage.startAnimation(rotationAnimation) },
                    if (delay) resources.getInteger(R.integer.slide_anim_duration).toLong() else 0)
        }
    }

    private fun hideLoading() {
        runOnUiThread {
            mYobaImage.clearAnimation()
            mYobaImage.setLayerType(View.LAYER_TYPE_NONE, null)
            mLoadingLayout.visibility = View.GONE
        }
    }

    private fun showError(throwable: Throwable) {
        runOnUiThread {
            mThreadListRecyclerView.visibility = View.GONE
            mErrorLayout.visibility = View.VISIBLE
            supportActionBar?.title = getString(R.string.error)
            val snackBar = Snackbar.make(mErrorLayout, throwable.message.toString(), Snackbar.LENGTH_INDEFINITE)
            snackBar.setAction(R.string.bljad, { snackBar.dismiss() })
            snackBar.show()
            mTryAgainButton.setOnClickListener {
                snackBar.dismiss()
                hideError()
                showLoading(false)
                loadThreads(false)
            }
        }
    }

    private fun hideError() {
        runOnUiThread {
            mErrorLayout.visibility = View.GONE
            mThreadListRecyclerView.visibility = View.VISIBLE
        }
    }

    private fun showThreadList() {
        runOnUiThread {
            val alpha = AlphaAnimation(0f, 1f)
            alpha.duration = resources.getInteger(R.integer.showing_list_duration).toLong()
            mThreadListRecyclerView.startAnimation(alpha)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unbindThreadListAdapterView()
        presenter.unbindView()
    }
}