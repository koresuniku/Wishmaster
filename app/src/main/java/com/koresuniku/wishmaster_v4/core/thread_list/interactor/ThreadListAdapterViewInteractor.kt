package com.koresuniku.wishmaster_v4.core.thread_list.interactor

import android.text.Html
import com.koresuniku.wishmaster_v4.core.base.rx.BaseRxAdapterViewInteractor
import com.koresuniku.wishmaster_v4.core.data.model.threads.ThreadListData
import com.koresuniku.wishmaster_v4.core.gallery.WishmasterImageUtils
import com.koresuniku.wishmaster_v4.core.thread_list.presenter.IThreadListPresenter
import com.koresuniku.wishmaster_v4.core.thread_list.view.ThreadItemView
import com.koresuniku.wishmaster_v4.core.thread_list.view.ThreadListAdapterView
import com.koresuniku.wishmaster_v4.core.util.text.WishmasterTextUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by koresuniku on 02.02.18.
 */
class ThreadListAdapterViewInteractor(compositeDisposable: CompositeDisposable,
                                      private val imageUtils: WishmasterImageUtils,
                                      private val textUtils: WishmasterTextUtils):
        BaseRxAdapterViewInteractor<
        IThreadListPresenter,
        ThreadListAdapterView<IThreadListPresenter>,
        ThreadItemView,
        ThreadListData>(compositeDisposable) {

    override fun setItemViewData(adapterView: ThreadListAdapterView<IThreadListPresenter>,
                                 view: ThreadItemView,
                                 data: ThreadListData,
                                 position: Int) {
        val thread = data.getThreadList()[position]

        //Subject
        thread.subject?.let {
            view.setSubject(textUtils.getSubjectSpanned(it, data.getBoardId()))
        }
        view.switchSubjectVisibility((thread.subject.isNullOrEmpty()))

        //Comment
        thread.comment?.let { view.setComment(textUtils.getSpannedFromHtml(it)) }

        //ShortInfo
        view.setThreadShortInfo(textUtils.getShortInfo(thread.postsCount, thread.filesCount))

        //Images
        thread.files?.let {
            when (adapterView.presenter.getThreadItemType(position)) {
                adapterView.SINGLE_IMAGE_CODE -> {
                    compositeDisposable.add(imageUtils.getImageItemData(it[0])
                            .subscribeOn(Schedulers.computation())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(view::setSingleImage))
                }
                adapterView.MULTIPLE_IMAGES_CODE -> {
                    compositeDisposable.add(imageUtils.getImageItemData(it)
                            .subscribeOn(Schedulers.computation())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(view::setMultipleImages))
                }
                else -> {}
            }
        }
    }
}