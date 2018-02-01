package com.koresuniku.wishmaster_v4.core.base.rx

import com.koresuniku.wishmaster_v4.core.base.mvp.BaseMvpPresenter
import com.koresuniku.wishmaster_v4.core.base.mvp.MvpPresenter
import com.koresuniku.wishmaster_v4.core.base.mvp.MvpView
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by koresuniku on 12.11.17.
 */

abstract class BaseRxPresenter<V: MvpView<*>> : BaseMvpPresenter<V>() {
    protected lateinit var compositeDisposable: CompositeDisposable

    override fun bindView(mvpView: V) {
        super.bindView(mvpView)
        compositeDisposable = CompositeDisposable()
    }

    override fun unbindView() {
        super.unbindView()
        compositeDisposable.clear()
    }
}