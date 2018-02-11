package com.koresuniku.wishmaster.core.modules.full_thread.presenter

import android.text.Html
import com.koresuniku.wishmaster.application.listener.OrientationNotifier
import com.koresuniku.wishmaster.core.dagger.IWishmasterDaggerInjector
import com.koresuniku.wishmaster.core.modules.full_thread.interactor.FullThreadNetworkInteractor
import com.koresuniku.wishmaster.core.modules.full_thread.view.FullThreadView
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
                                              orientationNotifier: OrientationNotifier):
        BaseFullThreadPresenter(compositeDisposable, networkInteractor, orientationNotifier) {
    private val LOG_TAG = FullThreadPresenter::class.java.simpleName

    override fun bindView(mvpView: FullThreadView<IFullThreadPresenter>) {
        super.bindView(mvpView)
        injector.daggerFullThreadPresenterComponent.inject(this)
    }

    override fun loadPostList() {
        compositeDisposable.add(networkInteractor.getDataFromNetwork()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (presenterData.postList.isEmpty()) mvpView?.showPostList()
                    presenterData = it
                    mvpView?.onPostListReceived(Html.fromHtml(it.postList[0].comment))
                }, { it.printStackTrace() }))
    }

    override fun onNetworkError(t: Throwable) {
        compositeDisposable.add(Completable.fromCallable {  }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t.printStackTrace(); mvpView?.showError(t.message)}))
    }

    override fun getBoardId() = mvpView?.getBoardId() ?: String()
    override fun getThreadNumber() = mvpView?.getThreadNumber() ?: String()
}