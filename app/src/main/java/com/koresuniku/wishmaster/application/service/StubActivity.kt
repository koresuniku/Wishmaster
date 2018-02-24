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

import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.application.WishmasterApplication
import com.koresuniku.wishmaster.application.singletones.WishmasterDownloadManager
import com.koresuniku.wishmaster.application.singletones.WishmasterPermissionManager
import com.koresuniku.wishmaster.application.utils.FirebaseKeystore
import com.koresuniku.wishmaster.core.dagger.module.application_scope.DaggerStubActivityComponent

import javax.inject.Inject

/**
 * Created by koresuniku on 2/21/18.
 */

class StubActivity : AppCompatActivity() {

    @Inject lateinit var downloadManager: WishmasterDownloadManager
    @Inject lateinit var permissionManager: WishmasterPermissionManager
    private lateinit var mVersionName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stub)
        DaggerStubActivityComponent.builder()
                .applicationComponent((application as WishmasterApplication).mDaggerApplicationComponent)
                .build()
                .inject(this)

        mVersionName = intent.getStringExtra(FirebaseKeystore.NEW_VERSION_NAME_KEY)

        if (permissionManager.checkAndRequestExternalStoragePermissionForLoadNewVersion(this)) {
            downloadManager.downloadWithNotification(FirebaseKeystore.PERSISTENT_DOWNLOAD_LINK, mVersionName)
            finish()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            WishmasterPermissionManager.WRITE_EXTERNAL_STORAGE_FOR_LOAD_NEW_VERSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    downloadManager.downloadWithNotification(FirebaseKeystore.PERSISTENT_DOWNLOAD_LINK, mVersionName)
                }
                finish()
            }
        }
    }


}