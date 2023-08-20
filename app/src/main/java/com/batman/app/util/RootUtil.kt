package com.batman.app.util

import android.content.Context
import android.os.Build
import android.os.UserManager
import android.provider.Settings
import androidx.annotation.RequiresApi
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader


object RootUtil {
    val isDeviceRooted: Boolean
        get() = checkRootMethod1() || checkRootMethod2() || checkRootMethod3()

    private fun checkRootMethod1(): Boolean {
        val buildTags = Build.TAGS
        return buildTags != null && buildTags.contains("test-keys")
    }

    private fun checkRootMethod2(): Boolean {
        val paths = arrayOf(
            "/system/app/Superuser.apk",
            "/sbin/su",
            "/system/bin/su",
            "/system/xbin/su",
            "/data/local/xbin/su",
            "/data/local/bin/su",
            "/system/sd/xbin/su",
            "/system/bin/failsafe/su",
            "/data/local/su",
            "/su/bin/su"
        )
        for (path in paths) {
            if (File(path).exists()) return true
        }
        return false
    }

    private fun checkRootMethod3(): Boolean {
        var process: Process? = null
        return try {
            process = Runtime.getRuntime().exec(arrayOf("/system/xbin/which", "su"))
            val `in` = BufferedReader(InputStreamReader(process.inputStream))
            `in`.readLine() != null
        } catch (t: Throwable) {
            false
        } finally {
            process?.destroy()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun isDevelopmentSettingsEnabled(context: Context): Boolean {
        val um = context.getSystemService(Context.USER_SERVICE) as UserManager
        val settingEnabled = Settings.Global.getInt(
            context.contentResolver,
            Settings.Global.DEVELOPMENT_SETTINGS_ENABLED,
            if (Build.TYPE == "eng") 1 else 0
        ) !== 0
        val hasRestriction = um.hasUserRestriction(
            UserManager.DISALLOW_DEBUGGING_FEATURES
        )
        val isAdmin: Boolean = um.isSystemUser
        return isAdmin && !hasRestriction && settingEnabled
    }
}


