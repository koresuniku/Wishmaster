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

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import javax.inject.Inject

/**
 * Created by koresuniku on 2/18/18.
 */
 
class WMPermissionManager @Inject constructor() {

    companion object {
        const val PERMISSION_REQUEST_KEY = "permission_request"
        const val WRITE_EXTERNAL_STORAGE_FOR_LOAD_NEW_VERSION_REQUEST_CODE = 228
    }

    fun checkWriteExternalStoragePermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
                context, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED
    }

    fun requestWriteExternalStoragePermission(activity: Activity, requestCode: Int) {
        ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                requestCode)
    }

    fun checkAndRequestExternalStoragePermissionForLoadNewVersion(activity: Activity): Boolean {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
           requestPermission(activity,
                   Manifest.permission.WRITE_EXTERNAL_STORAGE,
                   WRITE_EXTERNAL_STORAGE_FOR_LOAD_NEW_VERSION_REQUEST_CODE)
            return false
        }
        return true
    }

    private fun requestPermission(activity: Activity, permission: String, requestCode: Int) {
        ActivityCompat.requestPermissions(
                activity,
                arrayOf(permission),
                requestCode)
    }

}