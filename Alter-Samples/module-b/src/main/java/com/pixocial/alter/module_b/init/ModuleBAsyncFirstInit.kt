package com.pixocial.alter.module_b.init

import android.content.Context
import android.content.res.Configuration
import com.pixocial.alter.annotation.AlterLifeCycle
import com.pixocial.alter.core.lifecycle.IAlterLifeCycle
import com.pixocial.alter.enums.LifeCycleProcess
import com.pixocial.alter.enums.LifeCycleThread

@AlterLifeCycle(process = LifeCycleProcess.MAIN,
        callCreateThread = LifeCycleThread.IO,
        dependencies = "module_a:ModuleAAsyncFirstInit,module_a:ModuleAFirstInit,module_b:ModuleBFirstInit",
        priority = 6,
        description = "ModuleBAsyncFirstInit")
class ModuleBAsyncFirstInit : IAlterLifeCycle {

    override fun attachBaseContext(base: Context?) {
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
    }

    override fun onCreate(context: Context) {
        Thread.sleep(2000)
    }

    override fun onDependenciesCompleted(unitName: String) {
    }

    override fun onLowMemory() {
    }

    override fun onTrimMemory(level: Int) {
    }
}