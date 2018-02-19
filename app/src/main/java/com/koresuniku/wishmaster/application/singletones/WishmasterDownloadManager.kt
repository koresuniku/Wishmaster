/*
 * Copyright (c) 2018 koresuniku
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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