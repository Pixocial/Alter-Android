package com.pixocial.alter.core

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.pixocial.alter.core.router.AlterRouter

/**
 * Alter 入口类，用于初始化
 */
class Alter {
    companion object {

        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        var isDebug = false

        /**
         * application中的初始化类
         */
        @JvmStatic
        fun init(context: Context, isDebug: Boolean) {
            Log.d("Alter", "Alter init")
            this.context = context.applicationContext
            this.isDebug = isDebug
            AlterRouter.init()
        }
    }
}