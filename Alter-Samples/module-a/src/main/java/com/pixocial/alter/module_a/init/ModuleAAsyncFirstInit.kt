package com.pixocial.alter.module_a.init

import android.content.Context
import android.content.res.Configuration
import android.util.Log
import com.pixocial.alter.annotation.AlterLifeCycle
import com.pixocial.alter.core.lifecycle.IAlterLifeCycle
import com.pixocial.alter.enums.LifeCycleProcess
import com.pixocial.alter.enums.LifeCycleThread

@AlterLifeCycle(process = LifeCycleProcess.MAIN,
        callCreateThread = LifeCycleThread.IO,
        dependencies = "module_b:ModuleBFirstInit",
        priority = 3,
        description = "ModuleAAsyncFirstInit")
class ModuleAAsyncFirstInit : IAlterLifeCycle {
    override fun attachBaseContext(base: Context?) {
        Log.d("ModuleAAsyncFirstInit", "attachBaseContext")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        Log.d("ModuleAAsyncFirstInit", "onConfigurationChanged")
    }

    override fun onCreate(context: Context) {
        Log.d("ModuleAAsyncFirstInit", "onCreate")
    }

    override fun onDependenciesCompleted(unitName: String) {
        Log.d("ModuleAAsyncFirstInit", "onDependenciesCompleted unitName=$unitName")
    }

    override fun onLowMemory() {
        Log.d("ModuleAAsyncFirstInit", "onConfigurationChanged")
    }

    override fun onTrimMemory(level: Int) {
        Log.d("ModuleAAsyncFirstInit", "onConfigurationChanged")
    }
}