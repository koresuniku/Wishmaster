package com.koresuniku.wishmaster_v4.core.dagger


abstract class BaseDaggerMutableHolder<T> : IDaggerMutableHolder<T> {

    private var mObject: T = this.initObject()

    override fun getObject(): T = mObject

    override fun setObject(newObject: T) { mObject = newObject }
}