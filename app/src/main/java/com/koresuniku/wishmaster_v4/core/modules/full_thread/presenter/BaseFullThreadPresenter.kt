package com.koresuniku.wishmaster_v4.core.modules.full_thread.presenter

import com.koresuniku.wishmaster_v4.application.listener.OnOrientationChangedListener
import com.koresuniku.wishmaster_v4.application.listener.OrientationNotifier
import com.koresuniku.wishmaster_v4.core.base.rx.BaseRxDataPresenter
import com.koresuniku.wishmaster_v4.core.data.model.posts.PostListData
import com.koresuniku.wishmaster_v4.core.modules.full_thread.view.FullThreadView
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by koresuniku on 2/11/18.
 */

open class BaseFullThreadPresenter(compositeDisposable: CompositeDisposable,
                              private val orientationNotifier: OrientationNotifier) :
        BaseRxDataPresenter<FullThreadView<IFullThreadPresenter>, PostListData>(compositeDisposable),
        IFullThreadPresenter, OnOrientationChangedListener {

    override lateinit var presenterData: PostListData

    override fun onOrientationChanged(orientation: Int) {

    }
}