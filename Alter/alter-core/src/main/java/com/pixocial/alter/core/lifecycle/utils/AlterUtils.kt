package com.pixocial.alter.core.lifecycle.utils

import android.app.ActivityManager
import android.content.Context
import android.os.Process
import android.text.TextUtils

internal object AlterUtils {

    /**
     * 包名判断是否为主进程
     */
    fun isMainProcess(context: Context): Boolean {
        return TextUtils.equals(context.packageName, getProcessName(context))
    }

    /**
     * 获取进程全名
     */
    private fun getProcessName(context: Context): String? {
        val pid = Process.myPid()
        var currentProcessName = ""
        try {
            val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            for (process in manager.runningAppProcesses) {
                if (process.pid == pid) {
                    currentProcessName = process.processName
                    break
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return currentProcessName
    }
}