package com.koresuniku.wishmaster.application.listener

import com.koresuniku.wishmaster.core.network.github_api.Asset

/**
 * Created by koresuniku on 2/18/18.
 */

interface OnNewReleaseListener {
    fun onNewRelease(asset: Asset)
}