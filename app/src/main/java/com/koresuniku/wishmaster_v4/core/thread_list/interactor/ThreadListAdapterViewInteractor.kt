package com.koresuniku.wishmaster_v4.core.thread_list.interactor

import android.text.Html
import android.util.Log
import com.koresuniku.wishmaster_v4.application.shared_preferences.SharedPreferencesUiDimens
import com.koresuniku.wishmaster_v4.core.base.rx.BaseRxAdapterViewInteractor
import com.koresuniku.wishmaster_v4.core.data.model.threads.ThreadListData
import com.koresuniku.wishmaster_v4.core.gallery.WishmasterImageUtils
import com.koresuniku.wishmaster_v4.core.network.client.RetrofitHolder
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
                                      private val sharedPreferencesUiDimens: SharedPreferencesUiDimens,
                                      private val retrofitHolder: RetrofitHolder,
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
        thread.subject?.let { view.setSubject(textUtils.getSubjectSpanned(it, data.getBoardId())) }
        view.switchSubjectVisibility(!thread.subject.isNullOrBlank() && data.getBoardId() != "b")

        //Comment
        Log.d("TLAVI", "just before setting comment")
        thread.comment?.let {
            if (thread.files == null || thread.files?.size != 1)
                view.setComment(textUtils.getCommentDefault(it, sharedPreferencesUiDimens))
        }

        //ShortInfo
        view.setThreadShortInfo(textUtils.getShortInfo(thread.postsCount, thread.filesCount))

        //Images
        thread.files?.let {
            when (adapterView.presenter.getThreadItemType(position)) {
                adapterView.SINGLE_IMAGE_CODE -> {
                    compositeDisposable.add(imageUtils.getImageItemData(it[0])
                            .subscribeOn(Schedulers.computation())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    { view.setSingleImage(it, retrofitHolder.getBaseUrl(), imageUtils)
                                      view.setComment(textUtils.getCommentForSingleImageItem(
                                              thread.comment?:String(), sharedPreferencesUiDimens))
                                    }, { it.printStackTrace() }))
                }
                adapterView.MULTIPLE_IMAGES_CODE -> {
                    compositeDisposable.add(imageUtils.getImageItemData(it)
                            .subscribeOn(Schedulers.computation())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    { view.setMultipleImages(it, retrofitHolder.getBaseUrl(), imageUtils) },
                                    { it.printStackTrace() }))
                }
                else -> {}
            }
        }
    }
}