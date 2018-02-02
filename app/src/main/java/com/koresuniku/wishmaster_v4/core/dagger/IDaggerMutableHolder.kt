package com.koresuniku.wishmaster_v4.core.dagger


interface IDaggerMutableHolder<T> {

    fun initObject(): T
    fun getObject(): T
    fun setObject(newObject: T)
}