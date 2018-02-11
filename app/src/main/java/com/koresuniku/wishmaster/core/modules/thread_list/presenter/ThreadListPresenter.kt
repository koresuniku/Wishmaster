package com.koresuniku.wishmaster.core.modules.thread_list.presenter

import com.koresuniku.wishmaster.application.listener.OrientationNotifier
import com.koresuniku.wishmaster.core.dagger.IWishmasterDaggerInjector
import com.koresuniku.wishmaster.core.modules.thread_list.interactor.ThreadListAdapterViewInteractor
import com.koresuniku.wishmaster.core.modules.thread_list.interactor.ThreadListNetworkInteractor
import com.koresuniku.wishmaster.core.modules.thread_list.view.ThreadItemView
import com.koresuniku.wishmaster.core.modules.thread_list.view.ThreadListView
import io.reactivex.Completable
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
                                              threadListAdapterViewInteractor: ThreadListAdapterViewInteractor,
                                              orientationNotifier: OrientationNotifier):
        BaseThreadListPresenter(compositeDisposable, threadListNetworkInteractor, threadListAdapterViewInteractor, orientationNotifier) {
    private val LOG_TAG = ThreadListPresenter::class.java.simpleName

    override fun bindView(mvpView: ThreadListView<IThreadListPresenter>) {
        super.bindView(mvpView)
        injector.daggerThreadListPresenterComponent.inject(this)
    }

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

    override fun onNetworkError(t: Throwable) {
        compositeDisposable.add(Completable.fromCallable {  }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t.printStackTrace(); mView?.showError(t.message)}))
    }

    override fun setItemViewData(threadItemView: ThreadItemView, position: Int) {
        threadListAdapterView?.let {
            threadListAdapterViewInteractor.setItemViewData(it, threadItemView, presenterData, position)
        }
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

    override fun onThreadItemClicked(threadNumber: String) { mView?.launchFullThreadActivity(threadNumber) }
}
