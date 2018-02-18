package com.koresuniku.wishmaster.application.listener

import com.koresuniku.wishmaster.core.network.github_api.Asset
import javax.inject.Inject

/**
 * Created by koresuniku on 2/18/18.
 */

class NewReleaseNotifier @Inject constructor() {

    private val subscribers: MutableList<OnNewReleaseListener> = ArrayList()
    var cachedAsset: Asset? = null

    fun notifyNewVersion(asset: Asset) {
        cachedAsset = asset
        subscribers.forEach { it.onNewRelease(asset) }
    }

    fun bindListener(listener: OnNewReleaseListener) {
        subscribers.add(listener)
        cachedAsset?.let { listener.onNewRelease(it) }
    }

    fun unbindListener(listener: OnNewReleaseListener) {
        subscribers.remove(listener)
    }
}