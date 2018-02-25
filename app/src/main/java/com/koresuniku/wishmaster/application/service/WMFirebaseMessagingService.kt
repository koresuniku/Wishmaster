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
import android.app.Notification
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
import android.graphics.Color
import android.net.Uri
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.application.WishmasterApplication
import com.koresuniku.wishmaster.application.preferences.SharedPreferencesStorage
import com.koresuniku.wishmaster.application.singletones.WMDownloadManager
import com.koresuniku.wishmaster.application.singletones.WMPermissionManager
import com.koresuniku.wishmaster.application.utils.StubActivity
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function3
import java.util.*
import javax.inject.Inject


/**
 * Created by koresuniku on 2/21/18.
 */

class WMFirebaseMessagingService : FirebaseMessagingService() {

    @Inject lateinit var sharedPreferencesStorage: SharedPreferencesStorage

    override fun onMessageReceived(message: RemoteMessage?) {
        super.onMessageReceived(message)

        (application as WishmasterApplication)
                .mDaggerApplicationComponent
                .inject(this)

        message?.data?.get(FirebaseKeystore.NEW_VERSION_NAME_KEY)?.let {
            val switchSingle = sharedPreferencesStorage.readBoolean(
                    getString(R.string.switch_new_version_notif_key),
                    resources.getBoolean(R.bool.switch_new_version_notif_default))
            val soundSingle = sharedPreferencesStorage.readBoolean(
                    getString(R.string.new_version_notif_sound_key),
                    resources.getBoolean(R.bool.new_version_notif_sound_default))
            val vibrateSingle = sharedPreferencesStorage.readBoolean(
                    getString(R.string.new_version_notif_vibration_key),
                    resources.getBoolean(R.bool.new_version_notif_vibration_default))
            Single.zip(switchSingle, soundSingle, vibrateSingle,
                    Function3({ switch: Boolean, sound: Boolean, vibrate: Boolean ->
                        if (switch) sendNotification(it, sound, vibrate)
            })).subscribe()

        }
    }

    private fun sendNotification(versionName: String, sound: Boolean, vibrate: Boolean) {
        val notificationId = Random().nextInt(100)

        val intent = Intent(this, DownloadIntentService::class.java)
        intent.putExtra(FirebaseKeystore.NEW_VERSION_NAME_KEY, versionName)
        intent.putExtra(WMServiceUtils.NOTIFICATION_ID_KEY, notificationId)

        val pendingIntent = PendingIntent.getService(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT)

        val `when` = System.currentTimeMillis()
        val defaultSoundUri = Uri.parse("android.resource://$packageName/${R.raw.bratishka}")
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
                    .addAction(NotificationCompat.Action(
                            R.drawable.ic_file_download_black_24dp,
                            getString(R.string.download_text),
                            pendingIntent))
        }

        if (sound) mNotifyBuilder.setSound(defaultSoundUri)
        if (vibrate) mNotifyBuilder.setDefaults(Notification.DEFAULT_VIBRATE)
        mNotifyBuilder.setLights(Color.argb(1, 255, 165, 0), 3000, 3000)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, mNotifyBuilder.build())
    }

    class DownloadIntentService : IntentService(DownloadIntentService::class.java.simpleName) {
        @Inject lateinit var downloadManager: WMDownloadManager
        @Inject lateinit var permissionManager: WMPermissionManager

        override fun onHandleIntent(intent: Intent?) {
            (application as WishmasterApplication)
                    .mDaggerApplicationComponent
                    .inject(this)

            intent?.let {
                NotificationManagerCompat
                        .from(this)
                        .cancel(intent.getIntExtra(WMServiceUtils.NOTIFICATION_ID_KEY, -1))

                val name = intent.getStringExtra(FirebaseKeystore.NEW_VERSION_NAME_KEY)

                if (permissionManager.checkWriteExternalStoragePermission(this)) {
                    val link = FirebaseKeystore.PERSISTENT_DOWNLOAD_LINK
                    downloadManager.downloadWithNotification(link, name)
                } else {
                    val stubActivityIntent = Intent(applicationContext, StubActivity::class.java)
                    stubActivityIntent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_NEW_TASK
                    stubActivityIntent.putExtra(
                            WMPermissionManager.PERMISSION_REQUEST_KEY,
                            WMPermissionManager.WRITE_EXTERNAL_STORAGE_FOR_LOAD_NEW_VERSION_REQUEST_CODE)
                    stubActivityIntent.putExtra(FirebaseKeystore.NEW_VERSION_NAME_KEY, name)
                    startActivity(stubActivityIntent)
                }
           }
        }


    }
}