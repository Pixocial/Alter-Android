package com.pixocial.alter.plugins.utils

import org.gradle.api.Project
import org.gradle.api.initialization.Settings

class ApiUtils {

    public def static KEY_ALTER_INCLUDE_API_MODULES = "alterIncludeApiModules" //需要生成api-module工程的module列表

    def static API_MODULE_DIR = "alter-build-api" //生成的 api-module 存放目录

    /**
     * 根据Settings获取api-module路径
     * @param settings
     * @return
     */
    static String getApiLibraryPath(Settings settings) {
        settings.rootDir.absolutePath + File.separator + API_MODULE_DIR
    }

    /**
     * 根据module名称返回对应api module名称
     * @param moduleName
     * @return
     */
    static String getApiLibraryName(String moduleName) {
        return "${moduleName}-api"
    }

    /**
     * 获取module对应的api-module的源码目录
     * @param project
     * @return
     */
    static String getApiModuleSourceDir(Project project) {
        return project.getProjectDir().absolutePath + File.separator + "alter" + File.separator + "java"
    }

    /**
     * 判断是否是api module
     * @param moduleName
     * @return
     */
    static boolean isApiLibrary(String moduleName) {
        return moduleName.endsWith("-api")
    }
}