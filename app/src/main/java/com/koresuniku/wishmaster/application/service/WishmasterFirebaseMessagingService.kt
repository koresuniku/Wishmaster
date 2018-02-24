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
import com.koresuniku.wishmaster.application.utils.FirebaseKeystore
import java.lang.Exception
import android.app.NotificationManager
import android.app.PendingIntent
import android.graphics.BitmapFactory
import android.os.Build
import android.media.RingtoneManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v4.app.NotificationCompat
import com.koresuniku.wishmaster.R
import java.util.*




/**
 * Created by koresuniku on 2/21/18.
 */

class WishmasterFirebaseMessagingService : FirebaseMessagingService() {

    override fun onCreate() {
        super.onCreate()
    }

    override fun onMessageReceived(message: RemoteMessage?) {
        super.onMessageReceived(message)

        Log.d("WFMS", "onMessageReceived")

        message?.data?.get(FirebaseKeystore.NEW_VERSION_NAME_KEY)?.let {
            Log.d("", "new version name: $it")
            sendNotification(it)
        }
    }



    private fun sendNotification(versionName: String) {
        val intent = Intent(this, StubActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        intent.putExtra(FirebaseKeystore.NEW_VERSION_NAME_KEY, versionName)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
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
                    .setSmallIcon(R.drawable.ic_file_download_black_24dp)
                    .setWhen(`when`)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent)

        } else {
            mNotifyBuilder = NotificationCompat.Builder(this)
                    .setStyle(NotificationCompat.BigTextStyle().bigText(versionName))
                    .setContentTitle(getString(R.string.new_version_available)).setContentText(versionName)
                    .setSmallIcon(R.drawable.ic_file_download_black_24dp)
                    .setWhen(`when`)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent)
        }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(Random().nextInt(100), mNotifyBuilder.build())
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