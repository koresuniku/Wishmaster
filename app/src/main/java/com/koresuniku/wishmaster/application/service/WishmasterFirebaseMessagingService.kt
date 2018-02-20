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

package com.koresuniku.wishmaster.application.service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.koresuniku.wishmaster.application.singletones.WishmasterDownloadManager
import com.koresuniku.wishmaster.application.utils.FirebaseKeystore
import com.koresuniku.wishmaster.core.dagger.component.DaggerMessagingServiceComponent
import com.koresuniku.wishmaster.core.dagger.module.MessagingServiceModule
import java.lang.Exception
import javax.inject.Inject

/**
 * Created by koresuniku on 2/21/18.
 */

class WishmasterFirebaseMessagingService : FirebaseMessagingService() {

    @Inject lateinit var downloadManager: WishmasterDownloadManager

    override fun onCreate() {
        super.onCreate()
        DaggerMessagingServiceComponent.builder()
                .messagingServiceModule(MessagingServiceModule(this))
                .build()
                .inject(this)
    }

    override fun onMessageReceived(message: RemoteMessage?) {
        super.onMessageReceived(message)

        message?.data?.get(FirebaseKeystore.NEW_VERSION_NAME_KEY)?.let {
            Log.d("WFMS", "new version name: $it")
            downloadManager.downloadWithNotification(FirebaseKeystore.PERSISTENT_DOWNLOAD_LINK, it)
        }
    }

    override fun onMessageSent(p0: String?) {
        super.onMessageSent(p0)
    }

    override fun onDeletedMessages() {
        super.onDeletedMessages()
    }

    override fun onSendError(p0: String?, p1: Exception?) {
        super.onSendError(p0, p1)
    }
}