package com.koresuniku.wishmaster.application.singletones

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import javax.inject.Inject

/**
 * Created by koresuniku on 2/18/18.
 */
 
class WishmasterPermissionManager @Inject constructor() {

    companion object {
        const val WRITE_EXTERNAL_STORAGE_FOR_LOAD_NEW_VERSION_REQUEST_CODE = 228
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
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {}
        else {
            ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(permission),
                    requestCode)
        }
    }

}