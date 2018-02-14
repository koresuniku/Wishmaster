package com.koresuniku.wishmaster.core.modules.full_thread.presenter

import com.koresuniku.wishmaster.application.listener.OnOrientationChangedListener
import com.koresuniku.wishmaster.application.listener.OrientationNotifier
import com.koresuniku.wishmaster.core.base.rx.BaseRxDataPresenter
import com.koresuniku.wishmaster.core.data.model.posts.PostListData
import com.koresuniku.wishmaster.core.modules.full_thread.interactor.FullThreadNetworkInteractor
import com.koresuniku.wishmaster.core.modules.full_thread.view.FullThreadAdapterView
import com.koresuniku.wishmaster.core.modules.full_thread.view.FullThreadView
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by koresuniku on 2/11/18.
 */

abstract class BaseFullThreadPresenter(compositeDisposable: CompositeDisposable,
                                       protected val networkInteractor: FullThreadNetworkInteractor,
                                       private val orientationNotifier: OrientationNotifier) :
        BaseRxDataPresenter<FullThreadView<IFullThreadPresenter>, PostListData>(compositeDisposable),
        IFullThreadPresenter, OnOrientationChangedListener {
    override var fullThreadAdapterView: FullThreadAdapterView<IFullThreadPresenter>? = null
    override lateinit var presenterData: PostListData
    override fun isDataLoaded() = presenterData.postList.size != 0
    override fun getDataSize() = presenterData.postList.size

    override fun bindView(mvpView: FullThreadView<IFullThreadPresenter>) {
        super.bindView(mvpView)
        presenterData = PostListData.emptyData()
        networkInteractor.bindPresenter(this)
        orientationNotifier.bindListener(this)
    }

    override fun onOrientationChanged(orientation: Int) {
        fullThreadAdapterView?.onOrientationChanged(orientation)
    }

    override fun unbindView() {
        super.unbindView()
        networkInteractor.unbindPresenter()
        orientationNotifier.unbindListener(this)
    }

    override fun bindFullThreadAdapterView(fullThreadAdapterView: FullThreadAdapterView<IFullThreadPresenter>) {
        this.fullThreadAdapterView = fullThreadAdapterView
    }

    override fun unbindFullThreadAdapterView() {
        this.fullThreadAdapterView = null
    }
}