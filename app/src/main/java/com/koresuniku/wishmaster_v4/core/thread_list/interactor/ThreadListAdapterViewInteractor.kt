package com.koresuniku.wishmaster_v4.core.thread_list.interactor

import android.text.Html
import com.koresuniku.wishmaster_v4.core.base.rx.BaseRxAdapterViewInteractor
import com.koresuniku.wishmaster_v4.core.data.model.threads.Thread
import com.koresuniku.wishmaster_v4.core.thread_list.presenter.IThreadListPresenter
import com.koresuniku.wishmaster_v4.core.thread_list.view.ThreadItemView
import com.koresuniku.wishmaster_v4.core.thread_list.view.ThreadListAdapterView
import com.koresuniku.wishmaster_v4.core.thread_list.view.ThreadListView
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by koresuniku on 02.02.18.
 */
class ThreadListAdapterViewInteractor(compositeDisposable: CompositeDisposable) : BaseRxAdapterViewInteractor<
        IThreadListPresenter,
        ThreadListAdapterView<IThreadListPresenter>,
        ThreadItemView,
        Thread>(compositeDisposable) {

    override fun setItemViewData(adapterView: ThreadListAdapterView<IThreadListPresenter>,
                                 view: ThreadItemView,
                                 data: Thread,
                                 position: Int) {
        view.setComment(Html.fromHtml("Sup dvach murmurmurmur"))
    }
}