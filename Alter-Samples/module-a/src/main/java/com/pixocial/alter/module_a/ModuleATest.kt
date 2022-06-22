package com.pixocial.alter.module_a

import android.util.Log
import com.pixocial.alter.annotation.ServiceRegister
import com.pixocial.alter.core.service.IAlterServiceLifecycle
import com.pixocial.alter.module_a.api.IModuleATest

@ServiceRegister(serviceInterface = IModuleATest::class)
class ModuleATest : IModuleATest, IAlterServiceLifecycle {

    private val TAG = "ModuleATest"

    override fun test(): String {
        return "module a api"
    }

    override fun init() {
        Log.d(TAG, "ModuleATest init")
    }

    override fun unInit() {
        Log.d(TAG, "ModuleATest unInit")
    }
}