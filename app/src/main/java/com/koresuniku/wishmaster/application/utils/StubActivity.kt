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

package com.koresuniku.wishmaster.application.utils

import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.application.WMApplication
import com.koresuniku.wishmaster.application.global.WMDownloadManager
import com.koresuniku.wishmaster.application.global.WMPermissionManager
import javax.inject.Inject

/**
 * Created by koresuniku on 2/25/18.
 */

class StubActivity : AppCompatActivity() {

    @Inject lateinit var permissionManager: WMPermissionManager
    @Inject lateinit var downloadManager: WMDownloadManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stub)
        (application as WMApplication)
                .daggerApplicationComponent
                .inject(this)

        when (intent.getIntExtra(WMPermissionManager.PERMISSION_REQUEST_KEY, -1)) {
            WMPermissionManager.WRITE_EXTERNAL_STORAGE_FOR_LOAD_NEW_VERSION_REQUEST_CODE -> {
                permissionManager.requestWriteExternalStoragePermission(
                        this,
                        WMPermissionManager.WRITE_EXTERNAL_STORAGE_FOR_LOAD_NEW_VERSION_REQUEST_CODE)
            }
            else -> finish()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            WMPermissionManager.WRITE_EXTERNAL_STORAGE_FOR_LOAD_NEW_VERSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    val link = FirebaseKeystore.PERSISTENT_DOWNLOAD_LINK
                    val name = intent.getStringExtra(FirebaseKeystore.NEW_VERSION_NAME_KEY)
                    downloadManager.downloadWithNotification(link, name)
                }
                finish()
            }
            else -> finish()
        }
    }
}