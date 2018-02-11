package com.koresuniku.wishmaster.core.base.rx

import io.reactivex.disposables.CompositeDisposable


interface IRxObject {
    val compositeDisposable: CompositeDisposable
}