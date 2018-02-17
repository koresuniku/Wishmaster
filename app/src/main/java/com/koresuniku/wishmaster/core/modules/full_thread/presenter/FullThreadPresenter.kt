package com.koresuniku.wishmaster.core.modules.full_thread.presenter

import android.text.Html
import android.util.Log
import com.koresuniku.wishmaster.application.listener.OrientationNotifier
import com.koresuniku.wishmaster.core.dagger.IWishmasterDaggerInjector
import com.koresuniku.wishmaster.core.modules.full_thread.interactor.FullThreadAdapterViewInteractor
import com.koresuniku.wishmaster.core.modules.full_thread.interactor.FullThreadNetworkInteractor
import com.koresuniku.wishmaster.core.modules.full_thread.view.FullThreadView
import com.koresuniku.wishmaster.core.modules.full_thread.view.PostItemView
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by koresuniku on 2/11/18.
 */

class FullThreadPresenter @Inject constructor(private val injector: IWishmasterDaggerInjector,
                                              compositeDisposable: CompositeDisposable,
                                              networkInteractor: FullThreadNetworkInteractor,
                                              adapterViewInteractor: FullThreadAdapterViewInteractor,
                                              orientationNotifier: OrientationNotifier):
        BaseFullThreadPresenter(compositeDisposable, networkInteractor, adapterViewInteractor, orientationNotifier) {
    private val LOG_TAG = FullThreadPresenter::class.java.simpleName

    override fun bindView(mvpView: FullThreadView<IFullThreadPresenter>) {
        super.bindView(mvpView)
        injector.daggerFullThreadPresenterComponent.inject(this)
    }

    override fun loadPostList() {
        mvpView?.showLoading()
        compositeDisposable.add(networkInteractor
                .getDataFromNetwork()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    presenterData = it
                    fullThreadAdapterView?.onPostListDataChanged(it)
                    it.postList[0].let {
                        mvpView?.onPostListReceived(Html.fromHtml(
                                if (it.subject.isBlank()) it.comment else it.subject ) )
                    }
                }, { it.printStackTrace() }))
    }

    override fun loadNewPostList() {
        if (presenterData.postList.isEmpty()) {
            loadPostList()
        } else {
            compositeDisposable.add(networkInteractor
                    .getPostListDataFromPosition(presenterData.postList.size - 1)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
//                        presenterData = it
                        presenterData.postList[0].let {
                            mvpView?.onPostListReceived(Html.fromHtml(
                                    if (it.subject.isBlank()) it.comment else it.subject))
                        }
                    }, { it.printStackTrace() }))
        }
    }

    override fun onNetworkError(t: Throwable) {
        compositeDisposable.add(Completable.fromCallable {}
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t.printStackTrace(); mvpView?.showError(t.message) }))
    }

    override fun onNetworkNewPostsError(t: Throwable) {
        compositeDisposable.add(Completable.fromCallable {}
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t.printStackTrace(); mvpView?.showNewPostsError(t.message) }))
    }

    override fun getBoardId() = mvpView?.getBoardId() ?: String()
    override fun getThreadNumber() = mvpView?.getThreadNumber() ?: String()

    override fun getPostItemType(position: Int): Int {
        presenterData.postList[position].files?.let {
            return when (it.size) {
                0 -> fullThreadAdapterView?.NO_IMAGES_CODE ?: -1
                1 -> fullThreadAdapterView?.SINGLE_IMAGE_CODE ?: -1
                else -> fullThreadAdapterView?.MULTIPLE_IMAGES_CODE ?: -1
            }
        }
        return fullThreadAdapterView?.NO_IMAGES_CODE ?: -1
    }

    override fun setItemViewData(postItemView: PostItemView, position: Int) {
        fullThreadAdapterView?.let {
            adapterViewInteractor.setItemViewData(it, postItemView, presenterData, position)
        }
    }
}