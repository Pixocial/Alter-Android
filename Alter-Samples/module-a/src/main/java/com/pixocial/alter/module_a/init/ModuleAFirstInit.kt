package com.pixocial.alter.module_a.init

import android.content.Context
import android.content.res.Configuration
import com.pixocial.alter.annotation.AlterLifeCycle
import com.pixocial.alter.core.lifecycle.IAlterLifeCycle
import com.pixocial.alter.enums.LifeCycleProcess

@AlterLifeCycle(process = LifeCycleProcess.MAIN, priority = 6, description = "ModuleAFirstInit")
class ModuleAFirstInit : IAlterLifeCycle {
    override fun attachBaseContext(base: Context?) {
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
    }

    override fun onCreate(context: Context) {
    }

    override fun onDependenciesCompleted(initName: String) {
    }

    override fun onLowMemory() {
    }

    override fun onTrimMemory(level: Int) {
    }
}