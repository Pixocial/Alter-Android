package com.pixocial.alter.plugins

import com.android.build.gradle.AppExtension
import com.pixocial.alter.plugins.lifecycle.AlterLifeCycleTransform
import com.pixocial.alter.plugins.router.AlterRouteTransform
import com.pixocial.alter.plugins.utils.isAndroidApp
import com.pixocial.alter.plugins.utils.isAndroidLib
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Alter 字节码插桩 插件类
 */
class AlterCorePlugin : Plugin<Project>{
    override fun apply(project: Project) {
        if (!project.isAndroidApp() && !project.isAndroidLib()) {
            println("--> Alter AlterCorePlugin, not android project: ${project.name} !!!")
            return
        }
        println("--> Alter AlterCorePlugin apply")
        project.extensions.getByType(AppExtension::class.java).apply {
            registerTransform(AlterLifeCycleTransform(project))
            registerTransform(AlterRouteTransform())
        }
    }
}