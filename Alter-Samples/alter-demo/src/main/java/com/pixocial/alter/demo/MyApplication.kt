package com.pixocial.alter.demo

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import com.pixocial.alter.core.Alter
import com.pixocial.alter.core.BuildConfig
import com.pixocial.alter.core.lifecycle.AlterLifeCycleManager

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Alter.init(this, BuildConfig.DEBUG)

        AlterLifeCycleManager.instance.onCreate(this)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)

        AlterLifeCycleManager.instance.attachBaseContext(base)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        AlterLifeCycleManager.instance.onLowMemory()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        AlterLifeCycleManager.instance.onTrimMemory(level)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        AlterLifeCycleManager.instance.onConfigurationChanged(newConfig)
    }
}