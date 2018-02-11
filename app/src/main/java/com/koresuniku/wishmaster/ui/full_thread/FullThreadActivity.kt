package com.koresuniku.wishmaster.ui.full_thread

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import butterknife.BindView
import butterknife.ButterKnife
import com.koresuniku.wishmaster.R
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
    @BindView(R.id.post_list) lateinit var mThreadListRecyclerView: RecyclerView
    @BindView(R.id.background) lateinit var mBackground: ImageView

    override fun provideContentLayoutResource() = R.layout.activity_full_thread

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWishmasterApplication().daggerFullThreadViewComponent.inject(this)
        uiUtils.showSystemUI(this)
        ButterKnife.bind(this)
        presenter.bindView(this)


    }
}