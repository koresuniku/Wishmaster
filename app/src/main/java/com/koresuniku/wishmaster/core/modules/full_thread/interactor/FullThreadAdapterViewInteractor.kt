package com.koresuniku.wishmaster.core.modules.full_thread.interactor

import android.content.Context
import com.koresuniku.wishmaster.application.preferences.UiParams
import com.koresuniku.wishmaster.core.base.rx.BaseRxAdapterViewInteractor
import com.koresuniku.wishmaster.core.data.model.posts.PostListData
import com.koresuniku.wishmaster.core.data.model.threads.ThreadListData
import com.koresuniku.wishmaster.core.modules.full_thread.presenter.IFullThreadPresenter
import com.koresuniku.wishmaster.core.modules.full_thread.view.FullThreadAdapterView
import com.koresuniku.wishmaster.core.modules.full_thread.view.PostItemView
import com.koresuniku.wishmaster.core.modules.thread_list.presenter.IThreadListPresenter
import com.koresuniku.wishmaster.core.modules.thread_list.view.ThreadItemView
import com.koresuniku.wishmaster.core.modules.thread_list.view.ThreadListAdapterView
import com.koresuniku.wishmaster.core.network.client.RetrofitHolder
import com.koresuniku.wishmaster.core.utils.images.WishmasterImageUtils
import com.koresuniku.wishmaster.core.utils.text.WishmasterTextUtils
import com.koresuniku.wishmaster.ui.utils.ViewUtils
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/**
 * Created by koresuniku on 2/14/18.
 */

class FullThreadAdapterViewInteractor @Inject constructor(compositeDisposable: CompositeDisposable,
                                                          private val context: Context,
                                                          private val uiParams: UiParams,
                                                          private val retrofitHolder: RetrofitHolder,
                                                          private val imageUtils: WishmasterImageUtils,
                                                          private val textUtils: WishmasterTextUtils,
                                                          private val viewUtils: ViewUtils):
        BaseRxAdapterViewInteractor<
                IFullThreadPresenter,
                FullThreadAdapterView<IFullThreadPresenter>,
                PostItemView,
                PostListData>(compositeDisposable) {

    override fun setItemViewData(adapterView: FullThreadAdapterView<IFullThreadPresenter>,
                                 view: PostItemView, data: PostListData, position: Int) {

    }
}