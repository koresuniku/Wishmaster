package com.koresuniku.wishmaster_v4.core.base.rx

import io.reactivex.disposables.CompositeDisposable


interface IRxObject {
    val compositeDisposable: CompositeDisposable
}