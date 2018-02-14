package com.koresuniku.wishmaster.ui.full_thread

import android.animation.Animator
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.text.Spanned
import android.transition.Transition
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.LinearInterpolator
import android.widget.AbsListView
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
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

    @BindView(R.id.toolbar) lateinit var mToolbar: Toolbar
    @BindView(R.id.loading_layout) lateinit var mLoadingLayout: ViewGroup
    @BindView(R.id.yoba) lateinit var mYobaImage: ImageView
   // @BindView(R.id.progress_bar) lateinit var mProgressBar: ProgressBar
    @BindView(R.id.error_layout) lateinit var mErrorLayout: ViewGroup
    @BindView(R.id.try_again_button) lateinit var mTryAgainButton: Button
    @BindView(R.id.post_list) lateinit var mFullThreadRecyclerView: RecyclerView
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
        setupRecyclerView()

        //if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            presenter.loadPostList()
        //}
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

    override fun provideFromActivityRequestCode() = IntentKeystore.FROM_FULL_THREAD_ACTIVITY_REQUEST_CODE
    @LayoutRes override fun provideContentLayoutResource() = R.layout.activity_full_thread

    private fun setupToolbar() {
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupTitle(opComment: Spanned) { supportActionBar?.title = opComment }

    private fun setupRecyclerView() {
        wishmasterAnimationUtils.setFadeOutLayoutAnimation(mFullThreadRecyclerView)
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
        mFullThreadRecyclerView.adapter = mFullThreadRecyclerViewAdapter
    }

    override fun onEnterAnimationComplete() {
        super.onEnterAnimationComplete()

        mFullThreadRecyclerView.post {
            if (!isActivityReentered || (!presenter.isDataLoaded()) && isActivityReentered) {
                mFullThreadRecyclerView.scheduleLayoutAnimation()
            }
        }
    }

    override fun showLoading() {
        //mProgressBar.visibility = View.VISIBLE
        supportActionBar?.title = getString(R.string.loading_text)
        wishmasterAnimationUtils.showLoadingYoba(mYobaImage, mLoadingLayout)
    }

    override fun getBoardId() = intent.getStringExtra(IntentKeystore.BOARD_ID_CODE)
    override fun getThreadNumber() = intent.getStringExtra(IntentKeystore.THREAD_NUMBER_CODE)

    override fun onPostListReceived(opComment: Spanned) {
        hideLoading()
        setupTitle(opComment)
    }

    private fun hideLoading() {
//        mProgressBar.animate().setInterpolator(LinearInterpolator()).setDuration(100).alpha(0f)
//                .setListener(object : Animator.AnimatorListener {
//                    override fun onAnimationRepeat(p0: Animator?) { }
//                    override fun onAnimationEnd(p0: Animator?) {
//                        mProgressBar.visibility = View.GONE
//                    }
//                    override fun onAnimationCancel(p0: Animator?) {}
//                    override fun onAnimationStart(p0: Animator?) {}
//                })
        wishmasterAnimationUtils.hideLoadingYoba(mYobaImage, mLoadingLayout)
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

    private fun hideError() {
        mErrorLayout.visibility = View.GONE
        mFullThreadRecyclerView.visibility = View.VISIBLE
    }

    override fun onBackPressed() {
        presenter.unbindFullThreadAdapterView()
        super.onBackPressed()
    }

    override fun onDestroy() {
        presenter.unbindFullThreadAdapterView()
        super.onDestroy()
    }
}