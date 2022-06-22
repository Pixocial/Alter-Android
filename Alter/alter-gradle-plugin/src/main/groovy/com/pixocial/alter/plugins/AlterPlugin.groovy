package com.pixocial.alter.plugins

import com.pixocial.alter.plugins.utils.ApiRegulator
import com.pixocial.alter.plugins.utils.ApiUtils
import com.pixocial.alter.plugins.utils.DependencyConstant
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.artifacts.Configuration
import org.gradle.api.internal.artifacts.dependencies.DefaultProjectDependency

/**
 * 添加工程间的依赖
 */
class AlterPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        println "--> Alter AlterPlugin apply... ${project.displayName}"
        if (checkIsRootProject(project)) {
            project.afterEvaluate {
                addConfiguration(project)
                evaluateProjects(project)
                resolveApiModules(project)
            }
        }
    }

    /**
     * 是否是root project apply此插件
     * @param project
     * @return
     */
    private boolean checkIsRootProject(Project project) {
        if (project != project.getRootProject()) {
            project.logger.error "apply \"alter-api\" must be in Root Project's build.gradle"
            return false
        }
        return true
    }

    private void addConfiguration(Project root) {
        root.subprojects.each {
            if (!ApiUtils.isApiLibrary(it.name)) {
                it.beforeEvaluate { Project p ->

                    println "--> Alter addConfiguration ${p.name}"

                    Configuration alterApi = it.configurations.create(DependencyConstant.ALTER_API)
                    alterApi.canBeResolved = true
                    alterApi.canBeConsumed = true
                    alterApi.setVisible(false)
                    alterApi.allDependencies.all { dependency ->
                        if (dependency instanceof DefaultProjectDependency) {
                            println "---> Alter ${DependencyConstant.ALTER_API} >>>> name:${dependency.name}, group:${dependency.group}, version:${dependency.version}, projectDependency:${dependency instanceof DefaultProjectDependency}---"
                            println "---> Alter it.dependencies:${it.dependencies} root.project:${root.project((":${ApiUtils.getApiLibraryName(dependency.name)}"))}"
                            it.dependencies.add(JavaPlugin.API_CONFIGURATION_NAME, root.project((":${ApiUtils.getApiLibraryName(dependency.name)}")))
                        }
                    }

                    Configuration alterApiCompile = it.configurations.create(DependencyConstant.ALTER_API_COMPILE)
                    alterApiCompile.canBeResolved = true
                    alterApiCompile.canBeConsumed = true
                    alterApiCompile.setVisible(false)
                    alterApiCompile.allDependencies.all { dependency ->
                        Project apiProject = root.project(":${ApiUtils.getApiLibraryName(it.name)}")
                        if (apiProject != null) {
                            apiProject.afterEvaluate {
                                println "---> Alter ${DependencyConstant.ALTER_API_COMPILE}>>>>name:${dependency.name},group:${dependency.group},version:${dependency.version},projectDependency:${dependency instanceof DefaultProjectDependency}---"
                                apiProject.dependencies.add(JavaPlugin.API_CONFIGURATION_NAME, dependency)
                            }
                        }
                    }

                    Configuration alterApiImplementation = it.configurations.create(DependencyConstant.ALTER_API_IMPLEMENTATION)
                    alterApiImplementation.canBeResolved = true
                    alterApiImplementation.canBeConsumed = true
                    alterApiImplementation.setVisible(false)
                    alterApiImplementation.allDependencies.all { dependency ->
                        Project apiProject = root.project(":${ApiUtils.getApiLibraryName(it.name)}")
                        if (apiProject != null) {
                            apiProject.afterEvaluate {
                                println "---> Alter ${DependencyConstant.ALTER_API_IMPLEMENTATION}>>>>name:${dependency.name},group:${dependency.group},version:${dependency.version},projectDependency:${dependency instanceof DefaultProjectDependency}---"
                                apiProject.dependencies.add(JavaPlugin.IMPLEMENTATION_CONFIGURATION_NAME, dependency)
                            }
                        }
                    }

                    Configuration alterApiCompileOnly = it.configurations.create(DependencyConstant.ALTER_API_COMPILE_ONlY)
                    alterApiCompileOnly.canBeResolved = true
                    alterApiCompileOnly.canBeConsumed = true
                    alterApiCompileOnly.setVisible(false)
                    alterApiCompileOnly.allDependencies.all { dependency ->
                        Project apiProject = root.project(":${ApiUtils.getApiLibraryName(it.name)}")
                        if (apiProject != null) {
                            apiProject.afterEvaluate {
                                apiProject.dependencies.add(JavaPlugin.COMPILE_ONLY_CONFIGURATION_NAME, dependency)
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 对root以及所有子工程进行evaluate 配置
     * @param root 顶级工程
     */
    private void evaluateProjects(Project root) {
        root.subprojects.each {
            it.afterEvaluate { p ->
                if (!ApiUtils.isApiLibrary(it.name) && it.findProject(":${ApiUtils.getApiLibraryName(it.name)}") != null) {
                    println "--> Alter ${it.name} api: ${ApiUtils.getApiLibraryName(it.name)}"
                    it.dependencies.add(JavaPlugin.API_CONFIGURATION_NAME, it.project(":${ApiUtils.getApiLibraryName(it.name)}"))
                }
            }
        }
    }

    /**
     * 处理api-module的源码目录等
     * @param project
     */
    private void resolveApiModules(Project project) {
        ApiRegulator.resolveApiModules(project)
    }
}