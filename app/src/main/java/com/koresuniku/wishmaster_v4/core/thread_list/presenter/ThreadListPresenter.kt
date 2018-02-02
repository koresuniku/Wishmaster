package com.koresuniku.wishmaster_v4.core.thread_list.presenter

import com.koresuniku.wishmaster_v4.core.dagger.IWishmasterDaggerInjector
import com.koresuniku.wishmaster_v4.core.thread_list.interactor.ThreadListAdapterViewInteractor
import com.koresuniku.wishmaster_v4.core.thread_list.interactor.ThreadListNetworkInteractor
import com.koresuniku.wishmaster_v4.core.thread_list.view.ThreadItemView
import com.koresuniku.wishmaster_v4.core.thread_list.view.ThreadListView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by koresuniku on 01.01.18.
 */

class ThreadListPresenter @Inject constructor(private val injector: IWishmasterDaggerInjector,
                                              compositeDisposable: CompositeDisposable,
                                              threadListNetworkInteractor: ThreadListNetworkInteractor,
                                              threadListAdapterViewInteractor: ThreadListAdapterViewInteractor):
        BaseThreadListPresenter(compositeDisposable, threadListNetworkInteractor, threadListAdapterViewInteractor) {
    private val LOG_TAG = ThreadListPresenter::class.java.simpleName


    // @Inject lateinit var threadListApiService: ThreadListApiService
    //@Inject lateinit var databaseHelper: DatabaseHelper
    //@Inject lateinit var sharedPreferencesStorage: sharedPreferencesStorage

    //private lateinit var mLoadThreadListSingle: Single<ThreadListData>
    //private var mActualThreadListData: ThreadListData? = null
    //private var mThreadListAdapterView: ThreadListAdapterView<ThreadListPresenter>? = null

    override fun bindView(mvpView: ThreadListView<IThreadListPresenter>) {
        super.bindView(mvpView)
        injector.daggerThreadListPresenterComponent.inject(this)

        //mActualThreadListData = ThreadListData.emptyData()
//
//        mLoadThreadListSingle = getNewLoadThreadListSingle()
//        mLoadThreadListSingle = mLoadThreadListSingle.cache()
    }
//
//    fun bindThreadListAdapterView(threadListAdapterView: ThreadListAdapterView<ThreadListPresenter>) {
//        this.mThreadListAdapterView = threadListAdapterView
//    }

    override fun getBoardId(): String = mView?.getBoardId() ?: String()

    override fun loadThreadList() {
        compositeDisposable.add(threadListNetworkInteractor.getDataFromNetwork()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (presenterData.getThreadList().isEmpty()) mView?.showThreadList()
                    presenterData = it
                    mView?.onThreadListReceived(it.getBoardName())
                    threadListAdapterView?.onThreadListDataChanged(it)
                }, { it.printStackTrace(); mView?.showError(it.message) }))
    }


//
//    fun getLoadThreadsSingle(): Single<ThreadListData> = mLoadThreadListSingle
//
//    fun reloadThreads() { mLoadThreadListSingle = getNewLoadThreadListSingle().cache() }

//    private fun getNewLoadThreadListSingle(): Single<ThreadListData> {
//        return Single.create({ e -> kotlin.run {
//            compositeDisposable.add(loadThreadListDirectly()
//                    .subscribe({ schemaCatalog: ThreadListJsonSchemaCatalogResponse ->
//                        if (schemaCatalog.threads.isEmpty()) {
//                            Log.d(LOG_TAG, "schemaCatalog threads is empty!")
//                            compositeDisposable.add(loadThreadListByPages()
//                                    .subscribe(
//                                            { schemaByPages -> kotlin.run {
//                                                //val oldThreadListData = mActualThreadListData ?: ThreadListData.emptyData()
//                                                mActualThreadListData = ThreadListResponseParser.mapPageResponseToThreadListData(schemaByPages)
//                                                mActualThreadListData?.let {
//                                                    e.onSuccess(it)
//                                                    threadListAdapterView?.onThreadListDataChanged(it)
//                                                }
//                                            } },
//                                            { t -> e.onError(t) }))
//                        } else {
//                            ///val oldThreadListData = mActualThreadListData ?: ThreadListData.emptyData()
//                            mActualThreadListData = ThreadListResponseParser.mapCatalogResponseToThreadListData(schemaCatalog)
//                            mActualThreadListData?.let {
//                                e.onSuccess(it)
//                                threadListAdapterView?.onThreadListDataChanged(it)
//                            }
//                        }
//                    }, { t -> e.onError(t) }))
//        }})
//    }

//    private fun loadThreadListDirectly(): Single<ThreadListJsonSchemaCatalogResponse> {
//        return Single.create({ e -> kotlin.run {
//            mView?.let { compositeDisposable.add(
//                    threadListApiService.getThreadsObservable(it.getBoardId())
//                    .subscribe(
//                            { schema -> e.onSuccess(schema) },
//                            { throwable: Throwable -> e.onError(throwable) }
//                    ))
//            }
//        }})
//    }
//
//    private fun loadThreadListByPages(): Single<ThreadListJsonSchemaPageResponse> {
//        return Single.create({ e -> kotlin.run { mView?.let {
//            val boardId = it.getBoardId()
//            val indexResponse = threadListApiService.getThreadsByPageCall(boardId, "index").execute()
//            indexResponse.body()?.let {
//                Log.d(LOG_TAG, "raw pages: ${it.pages}")
//                it.threads = arrayListOf()
//                //Абу, почини API!
//                for (i in 1 until it.pages.size - 1) {
//                    val nextPageResponse = threadListApiService.getThreadsByPageCall(boardId, i.toString()).execute()
//                    Log.d(LOG_TAG, "inside pages count: $i, threads: ${nextPageResponse.body()?.threads?.size}")
//                    it.threads.addAll(nextPageResponse.body()?.threads ?: emptyList())
//                }
//                e.onSuccess(it)
//            }
//        }
//        }})
//    }

    override fun setItemViewData(threadItemView: ThreadItemView, position: Int) {
        threadListAdapterView?.let {
            threadListAdapterViewInteractor.setItemViewData(
                    it, threadItemView, presenterData.getThreadList()[position], position)
        }
//        presenterData.getThreadList()?.let {
//            val thread = it[position]
//            mView?.let {
//                threadItemView.setSubject(WishmasterTextUtils.getSubjectSpanned(
//                        thread.subject ?: "", it.getBoardId()),
//                        thread.files?.isNotEmpty() ?: false)
//                threadItemView.setComment(WishmasterTextUtils.getSpannedFromHtml(thread.comment ?: ""))
//                threadItemView.setResumeInfo(WishmasterTextUtils.getResumeInfo(thread.postsCount, thread.filesCount))
//                thread.files?.let {
////                    when (getThreadItemType(position)) {
////                        SINGLE_IMAGE_CODE ->
////                            compositeDisposable.add(WishmasterImageUtils
////                                    .getImageItemData(it[0], ISharedPreferencesStorage, compositeDisposable)
////                                    .subscribeOn(Schedulers.computation())
////                                    .observeOn(AndroidSchedulers.mainThread())
////                                    .subscribe(threadItemView::setSingleImage))
////                        MULTIPLE_IMAGES_CODE ->
////                            compositeDisposable.add(WishmasterImageUtils
////                                    .getImageItemData(it, ISharedPreferencesStorage, compositeDisposable)
////                                    .subscribeOn(Schedulers.computation())
////                                    .observeOn(AndroidSchedulers.mainThread())
////                                    .subscribe(threadItemView::setMultipleImages))
////                        else -> {}
////                    }
//                    }
//                }
//            }
    }

    override fun getThreadListDataSize() = presenterData.getThreadList().size

    override fun getThreadItemType(position: Int): Int {
        presenterData.getThreadList()[position].files?.let {
                return when (it.size) {
                    0 -> threadListAdapterView?.NO_IMAGES_CODE ?: -1
                    1 -> threadListAdapterView?.SINGLE_IMAGE_CODE ?: -1
                    else -> threadListAdapterView?.MULTIPLE_IMAGES_CODE ?: -1
                }
            }
        return threadListAdapterView?.NO_IMAGES_CODE ?: -1
    }
}
