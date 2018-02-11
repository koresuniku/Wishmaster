package com.koresuniku.wishmaster.application.listener

import javax.inject.Inject

/**
 * Created by koresuniku on 2/10/18.
 */
class OrientationNotifier @Inject constructor() {

    private val subscribers: MutableList<OnOrientationChangedListener> = ArrayList()

    fun notifyOrientation(orientation: Int) {
        subscribers.forEach { it.onOrientationChanged(orientation) }
    }

    fun bindListener(listener: OnOrientationChangedListener) {
        subscribers.add(listener)
    }

    fun unbindListener(listener: OnOrientationChangedListener) {
        subscribers.remove(listener)
    }
}