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

import android.app.IntentService
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.koresuniku.wishmaster.application.utils.FirebaseKeystore
import java.lang.Exception
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.graphics.BitmapFactory
import android.os.Build
import android.media.RingtoneManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.application.WishmasterApplication
import com.koresuniku.wishmaster.application.singletones.WMDownloadManager
import java.util.*
import javax.inject.Inject


/**
 * Created by koresuniku on 2/21/18.
 */

class WMFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage?) {
        super.onMessageReceived(message)

        Log.d("WFMS", "onMessageReceived")

        message?.data?.get(FirebaseKeystore.NEW_VERSION_NAME_KEY)?.let {
            Log.d("", "new version name: $it")
            sendNotification(it)
        }
    }

    private fun sendNotification(versionName: String) {
        val notificationId = Random().nextInt(100)

        val intent = Intent(this, DownloadIntentService::class.java)
        intent.putExtra(FirebaseKeystore.NEW_VERSION_NAME_KEY, versionName)
        intent.putExtra(WMServiceUtils.NOTIFICATION_ID_KEY, notificationId)

        val pendingIntent = PendingIntent.getService(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT)

        val `when` = System.currentTimeMillis()
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val mNotifyBuilder: NotificationCompat.Builder
        val lollipop = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
        if (lollipop) {
            mNotifyBuilder = NotificationCompat.Builder(this)
                    .setContentTitle(getString(R.string.new_version_available))
                    .setStyle(NotificationCompat.BigTextStyle().bigText(versionName))
                    .setContentText(versionName)
                    .setColor(Color.TRANSPARENT)
                    .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.wishmaster_logo))
                    .setSmallIcon(R.drawable.wishmaster_logo)
                    .setWhen(`when`)
                    .setSound(defaultSoundUri)
                    .addAction(NotificationCompat.Action(
                            R.drawable.ic_file_download_black_24dp,
                            getString(R.string.download_text),
                            pendingIntent))

        } else {
            mNotifyBuilder = NotificationCompat.Builder(this)
                    .setStyle(NotificationCompat.BigTextStyle().bigText(versionName))
                    .setContentTitle(getString(R.string.new_version_available)).setContentText(versionName)
                    .setSmallIcon(R.drawable.wishmaster_logo)
                    .setWhen(`when`)
                    .setSound(defaultSoundUri)
                    .addAction(NotificationCompat.Action(
                            R.drawable.ic_file_download_black_24dp,
                            getString(R.string.download_text),
                            pendingIntent))
        }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, mNotifyBuilder.build())
    }

    class DownloadIntentService : IntentService(DownloadIntentService::class.java.simpleName) {

        @Inject lateinit var downloadManager: WMDownloadManager

        override fun onHandleIntent(intent: Intent?) {
            (application as WishmasterApplication)
                    .mDaggerApplicationComponent
                    .inject(this)

            intent?.let {
                NotificationManagerCompat
                        .from(this)
                        .cancel(intent.getIntExtra(WMServiceUtils.NOTIFICATION_ID_KEY, -1))

                val link = FirebaseKeystore.PERSISTENT_DOWNLOAD_LINK
                val name = intent.getStringExtra(FirebaseKeystore.NEW_VERSION_NAME_KEY)

                downloadManager.downloadWithNotification(link, name)
           }
        }
    }
}