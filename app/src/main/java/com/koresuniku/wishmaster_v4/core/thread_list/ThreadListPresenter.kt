package com.koresuniku.wishmaster_v4.core.thread_list

import android.util.Log
import com.koresuniku.wishmaster_v4.application.SharedPreferencesStorage
import com.koresuniku.wishmaster_v4.core.base.BaseRxPresenter
import com.koresuniku.wishmaster_v4.core.data.database.DatabaseHelper
import com.koresuniku.wishmaster_v4.core.data.threads.ThreadListData
import com.koresuniku.wishmaster_v4.core.data.threads.ThreadsMapper
import com.koresuniku.wishmaster_v4.core.domain.thread_list_api.ThreadListApiService
import com.koresuniku.wishmaster_v4.core.domain.thread_list_api.ThreadListJsonSchemaCatalogResponse
import com.koresuniku.wishmaster_v4.core.domain.thread_list_api.ThreadListJsonSchemaPageResponse
import com.koresuniku.wishmaster_v4.core.gallery.WishmasterImageUtils
import com.koresuniku.wishmaster_v4.core.util.text.WishmasterTextUtils
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by koresuniku on 01.01.18.
 */

class ThreadListPresenter @Inject constructor(): BaseRxPresenter<ThreadListView>() {
    private val LOG_TAG = ThreadListPresenter::class.java.simpleName

    @Inject lateinit var threadListApiService: ThreadListApiService
    @Inject lateinit var databaseHelper: DatabaseHelper
    @Inject lateinit var sharedPreferencesStorage: SharedPreferencesStorage

    private lateinit var mLoadThreadListSingle: Single<ThreadListData>
    private var mActualThreadListData: ThreadListData? = null
    private var mThreadListAdapterView: ThreadListAdapterView? = null

    override fun bindView(mvpView: ThreadListView) {
        super.bindView(mvpView)
        mvpView.getWishmasterApplication().getThreadListComponent().inject(this)

        mActualThreadListData = ThreadListData.emptyData()

        mLoadThreadListSingle = getNewLoadThreadListSingle()
        mLoadThreadListSingle = mLoadThreadListSingle.cache()
    }

    fun bindThreadListAdapterView(threadListAdapterView: ThreadListAdapterView) {
        this.mThreadListAdapterView = threadListAdapterView
    }

    fun getLoadThreadsSingle(): Single<ThreadListData> = mLoadThreadListSingle

    fun reloadThreads() { mLoadThreadListSingle = getNewLoadThreadListSingle().cache() }

    private fun getNewLoadThreadListSingle(): Single<ThreadListData> {
        return Single.create({ e -> kotlin.run {
            compositeDisposable.add(loadThreadListDirectly()
                    .subscribe({ schemaCatalog: ThreadListJsonSchemaCatalogResponse ->
                        if (schemaCatalog.threads.isEmpty()) {
                            Log.d(LOG_TAG, "schemaCatalog threads is empty!")
                            compositeDisposable.add(loadThreadListByPages()
                                    .subscribe(
                                            { schemaByPages -> kotlin.run {
                                                //val oldThreadListData = mActualThreadListData ?: ThreadListData.emptyData()
                                                mActualThreadListData = ThreadsMapper.mapPageResponseToThreadListData(schemaByPages)
                                                mActualThreadListData?.let {
                                                    e.onSuccess(it)
                                                    mThreadListAdapterView?.onThreadListDataChanged(it)
                                                }
                                            } },
                                            { t -> e.onError(t) }))
                        } else {
                            ///val oldThreadListData = mActualThreadListData ?: ThreadListData.emptyData()
                            mActualThreadListData = ThreadsMapper.mapCatalogResponseToThreadListData(schemaCatalog)
                            mActualThreadListData?.let {
                                e.onSuccess(it)
                                mThreadListAdapterView?.onThreadListDataChanged(it)
                            }
                        }
                    }, { t -> e.onError(t) }))
        }})
    }

    private fun loadThreadListDirectly(): Single<ThreadListJsonSchemaCatalogResponse> {
        return Single.create({ e -> kotlin.run {
            mView?.let { compositeDisposable.add(
                    threadListApiService.getThreadsObservable(it.getBoardId())
                    .subscribe(
                            { schema -> e.onSuccess(schema) },
                            { throwable: Throwable -> e.onError(throwable) }
                    ))
            }
        }})
    }

    private fun loadThreadListByPages(): Single<ThreadListJsonSchemaPageResponse> {
        return Single.create({ e -> kotlin.run { mView?.let {
            val boardId = it.getBoardId()
            val indexResponse = threadListApiService.getThreadsByPageCall(boardId, "index").execute()
            indexResponse.body()?.let {
                Log.d(LOG_TAG, "raw pages: ${it.pages}")
                it.threads = arrayListOf()
                //Абу, почини API!
                for (i in 1 until it.pages.size - 1) {
                    val nextPageResponse = threadListApiService.getThreadsByPageCall(boardId, i.toString()).execute()
                    Log.d(LOG_TAG, "inside pages count: $i, threads: ${nextPageResponse.body()?.threads?.size}")
                    it.threads.addAll(nextPageResponse.body()?.threads ?: emptyList())
                }
                e.onSuccess(it)
            }
        }
        }})
    }

    fun bindThreadItemViewByPosition(threadItemView: ThreadItemView, position: Int) {
        mActualThreadListData?.getThreadList()?.let {
            val thread = it[position]
            mView?.let {
                threadItemView.setSubject(WishmasterTextUtils.getSubjectSpanned(
                        thread.subject ?: "", it.getBoardId()),
                        thread.files?.isNotEmpty() ?: false)
                threadItemView.setComment(WishmasterTextUtils.getSpannedFromHtml(thread.comment ?: ""))
                threadItemView.setResumeInfo(WishmasterTextUtils.getResumeInfo(thread.postsCount, thread.filesCount))
                thread.files?.let {
                    when (getThreadItemType(position)) {
                        SINGLE_IMAGE_CODE ->
                            compositeDisposable.add(WishmasterImageUtils
                                    .getImageItemData(it[0], sharedPreferencesStorage, compositeDisposable)
                                    .subscribeOn(Schedulers.computation())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(threadItemView::setSingleImage))
                        MULTIPLE_IMAGES_CODE ->
                            compositeDisposable.add(WishmasterImageUtils
                                    .getImageItemData(it, sharedPreferencesStorage, compositeDisposable)
                                    .subscribeOn(Schedulers.computation())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(threadItemView::setMultipleImages))
                        else -> {}
                    }
                    }
                }
            }
    }

    fun getThreadListDataSize() = mActualThreadListData?.getThreadList()?.size ?: 0

    companion object {
        val ERROR_CODE = -1
        val NO_IMAGES_CODE = 0
        val SINGLE_IMAGE_CODE = 1
        val MULTIPLE_IMAGES_CODE = 2
    }

    fun getThreadItemType(position: Int): Int {
        mActualThreadListData?.let {
            it.getThreadList()[position].files?.let {
                return when (it.size) {
                    0 -> NO_IMAGES_CODE
                    1 -> SINGLE_IMAGE_CODE
                    else -> MULTIPLE_IMAGES_CODE
                }
            }
            return NO_IMAGES_CODE
        }
        return ERROR_CODE
    }

    override fun unbindView() {
        super.unbindView()
        reloadThreads()
        databaseHelper.readableDatabase.close()
    }

    fun unbindThreadListAdapterView() {
        this.mThreadListAdapterView = null
    }
}
