package com.koresuniku.wishmaster.application.listener

/**
 * Created by koresuniku on 2/18/18.
 */

class NewReleaseNotifier {

    private val subscribers: MutableList<OnNewReleaseListener> = ArrayList()

    fun notifyNewVersion() {

    }

    fun bindListener(listener: OnNewReleaseListener) {
        subscribers.add(listener)
    }

    fun unbindListener(listener: OnNewReleaseListener) {
        subscribers.remove(listener)
    }
}