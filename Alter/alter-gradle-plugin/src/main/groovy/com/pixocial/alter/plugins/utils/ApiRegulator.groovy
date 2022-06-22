package com.pixocial.alter.plugins.utils

import org.gradle.api.Project

class ApiRegulator {

    /**
     * 对各个生成的api-module进行configuration
     * @param root
     */
    static void resolveApiModules(Project root) {
        root.subprojects { Project it ->
            configureSourceSets(it)
            if (ApiUtils.isApiLibrary(it.name)) {
                configurePlugin(it)
            }
        }
    }

    /**
     * 对所有的api-module配置对应的sourceSets
     * @param implProject
     */
    private static void configureSourceSets(Project implProject) {
        if (ApiUtils.isApiLibrary(implProject.name)) return
        implProject.afterEvaluate {
            String alterSrcDir = ApiUtils.getApiModuleSourceDir(it)
            Project apiProject = it.rootProject.findProject(":${ApiUtils.getApiLibraryName(it.name)}")
            if (apiProject != null && alterSrcDir != null && !alterSrcDir.isEmpty()) {
                println "--> Alter configureSourceSets srcProject:${implProject.name} apiProject:${apiProject.name} srcDir:${alterSrcDir}"
                apiProject.afterEvaluate {
                    apiProject.android.sourceSets.main.java.srcDirs += [alterSrcDir]
                }
            }
        }
    }

    /**
     * 配置api-module需要apply的gradle-plugin
     *
     * @param apiProject
     */
    private static void configurePlugin(Project apiProject) {
        apiProject.beforeEvaluate { Project it ->
            it.apply plugin: 'com.android.library'
            it.apply plugin: 'kotlin-android'
        }
    }

}