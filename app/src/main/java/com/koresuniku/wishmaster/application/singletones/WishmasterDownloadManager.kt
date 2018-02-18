package com.koresuniku.wishmaster.application.singletones

import javax.inject.Inject
import android.content.Context.DOWNLOAD_SERVICE
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment

/**
 * Created by koresuniku on 2/18/18.
 */

class WishmasterDownloadManager @Inject constructor(private val context: Context) {

    fun downloadWithNotification(link: String, name: String) {
        val uri = Uri.parse(link)
        val r = DownloadManager.Request(uri)

        r.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, name)
        r.allowScanningByMediaScanner()
        r.setShowRunningNotification(true)
        r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

        val dm = context.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        dm.enqueue(r)
    }
}