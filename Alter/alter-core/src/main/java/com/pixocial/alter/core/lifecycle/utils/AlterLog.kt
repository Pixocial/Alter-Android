package com.pixocial.alter.core.lifecycle.utils

import android.util.Log
import com.pixocial.alter.core.Alter

internal object AlterLog {

    fun logD(tag: String?, msg: String?) {
        if (Alter.isDebug) {
            return
        }
        msg?.let {
            Log.d(tag, it)
        }
    }

    fun logW(tag: String?, msg: String?) {
        if (Alter.isDebug) {
            return
        }
        msg?.let {
            Log.w(tag, it)
        }
    }

    fun logE(tag: String?, msg: String?) {
        if (Alter.isDebug) {
            return
        }
        msg?.let {
            Log.e(tag, it)
        }
    }
}