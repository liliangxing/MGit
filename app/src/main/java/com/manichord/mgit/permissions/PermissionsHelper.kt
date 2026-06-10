package com.manichord.mgit.permissions

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.core.content.ContextCompat

// Courtesy of VLC
// ref: https://github.com/videolan/vlc-android/commit/62897067a0fcaf02140deeafb1f93cb7c90c9fc8#diff-8c75f7d01dd35f6b14dee21e963e99911b5c45c5c5629f74dced6e16336689f1
class PermissionsHelper {

    companion object {
        /**
         * Check if the app has the [Manifest.permission.MANAGE_EXTERNAL_STORAGE] granted
         */
        fun isExternalStorageManager(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R &&
                Environment.isExternalStorageManager()

        fun canReadStorage(context: Context): Boolean {
            return Build.VERSION.SDK_INT <= Build.VERSION_CODES.M ||
                    ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED || isExternalStorageManager()
        }

        /**
         * Check if the device supports the all files access permission intent
         * Returns false on Android 15+ where this permission is restricted
         */
        fun canRequestAllFilesAccess(context: Context): Boolean {
            val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
            } else {
                Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            }

            val resolveInfo: List<ResolveInfo> = context.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
            return resolveInfo.isNotEmpty()
        }
    }
}




