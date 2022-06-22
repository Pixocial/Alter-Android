package com.pixocial.alter.core.lifecycle

import android.content.Context
import android.content.res.Configuration

/**
 * application 声明周期接口类
 *
 * 模块中实现此接口，会自动回调其生命周期的相关方法
 */
interface IAlterLifeCycle {

    fun attachBaseContext(base: Context?)

    fun onCreate(context: Context)

    fun onLowMemory()

    fun onTrimMemory(level: Int)

    fun onConfigurationChanged(newConfig: Configuration)

    fun onDependenciesCompleted(unitName: String)
}