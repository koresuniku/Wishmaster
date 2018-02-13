package com.koresuniku.wishmaster.ui.full_thread

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.text.Spanned
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import butterknife.BindView
import butterknife.ButterKnife
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.application.IntentKeystore
import com.koresuniku.wishmaster.core.modules.full_thread.presenter.IFullThreadPresenter
import com.koresuniku.wishmaster.core.modules.full_thread.view.FullThreadView
import com.koresuniku.wishmaster.core.utils.text.WishmasterTextUtils
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

    @BindView(R.id.toolbar) lateinit var mToolbar: Toolbar
    @BindView(R.id.loading_layout) lateinit var mLoadingLayout: ViewGroup
    @BindView(R.id.yoba) lateinit var mYobaImage: ImageView
    @BindView(R.id.error_layout) lateinit var mErrorLayout: ViewGroup
    @BindView(R.id.try_again_button) lateinit var mTryAgainButton: Button
    @BindView(R.id.post_list) lateinit var mPostListRecyclerView: RecyclerView
    @BindView(R.id.background) lateinit var mBackground: ImageView

    override fun provideContentLayoutResource() = R.layout.activity_full_thread

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWishmasterApplication().daggerFullThreadViewComponent.inject(this)
        uiUtils.showSystemUI(this)
        ButterKnife.bind(this)
        presenter.bindView(this)

        setupToolbar()

        showLoading(true)
        presenter.loadPostList()
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_OK)
        super.onBackPressed()
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

    private fun setupToolbar() {
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupTitle(opComment: Spanned) {
        supportActionBar?.title = opComment
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

    override fun getBoardId() = intent.getStringExtra(IntentKeystore.BOARD_ID_CODE)
    override fun getThreadNumber() = intent.getStringExtra(IntentKeystore.THREAD_NUMBER_CODE)

    override fun onPostListReceived(opComment: Spanned) {
        hideLoading()
        setupTitle(opComment)
    }

    private fun hideLoading() {
        mYobaImage.clearAnimation()
        mYobaImage.setLayerType(View.LAYER_TYPE_NONE, null)
        mLoadingLayout.visibility = View.GONE
    }

    override fun showError(message: String?) {
        hideLoading()

        mPostListRecyclerView.visibility = View.GONE
        mErrorLayout.visibility = View.VISIBLE
        supportActionBar?.title = getString(R.string.error)
        val snackBar = Snackbar.make(
                mErrorLayout,
                message ?: getString(R.string.error),
                Snackbar.LENGTH_INDEFINITE)
        snackBar.setAction(R.string.bljad, { snackBar.dismiss() })
        snackBar.show()
        mTryAgainButton.setOnClickListener {
            snackBar.dismiss()
            hideError()
            showLoading(false)
            presenter.loadPostList()
        }
    }

    private fun hideError() {
        mErrorLayout.visibility = View.GONE
        mPostListRecyclerView.visibility = View.VISIBLE
    }

    override fun showPostList() {
//        val alpha = AlphaAnimation(0f, 1f)
//        alpha.duration = resources.getInteger(R.integer.showing_list_duration).toLong()
//        mPostListRecyclerView.startAnimation(alpha)
    }
}