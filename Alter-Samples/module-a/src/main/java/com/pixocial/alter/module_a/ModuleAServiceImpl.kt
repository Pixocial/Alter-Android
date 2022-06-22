package com.pixocial.alter.module_a

import com.pixocial.alter.annotation.ServiceRegister
import com.pixocial.alter.module_a.api.IModuleAService

@ServiceRegister(serviceInterface = IModuleAService::class)
class ModuleAServiceImpl : IModuleAService {
    override fun test(): String {
        return "Hello, ModuleAServiceImpl!"
    }
}