package com.pixocial.alter.plugins

import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings
import org.gradle.initialization.DefaultProjectDescriptor
import com.pixocial.alter.plugins.utils.ApiUtils

/**
 * 生成对应的api工程，并在settings.gradle中include
 */
class AlterInitializationPlugin implements Plugin<Settings> {

    @Override
    void apply(Settings settings) {
        println "--> Alter AlterInitializationPlugin apply"

        settings.gradle.settingsEvaluated { Settings s ->

            def alterIncludeApiModules = s.startParameter.projectProperties.get(ApiUtils.KEY_ALTER_INCLUDE_API_MODULES)
            if (alterIncludeApiModules == null) {
                alterIncludeApiModules = []
            }

            println "--> Alter alterIncludeApiModules : ${alterIncludeApiModules}"

            s.getProjectRegistry().getAllProjects().each { DefaultProjectDescriptor descriptor ->

                if (alterIncludeApiModules.contains(descriptor.name)) {
                    genApiModule(s, descriptor.name, descriptor.projectDir.path)
                    includeApiModule(s, descriptor.name)
                }
            }
        }
    }

    /**
     * 根据module创建对应的module-api工程
     * @param settings
     * @param moduleName
     * @param path
     */
    private void genApiModule(Settings settings, String moduleName, String path) {
        println "--> alter genApiModule ${moduleName} ${path}"

        File api_module = new File(ApiUtils.getApiLibraryPath(settings), "${ApiUtils.getApiLibraryName(moduleName)}")
        if (!api_module.exists()) {
            if (!api_module.mkdirs()) {
                println "--> alter create Directory ${api_module.absolutePath} fail and return in genApiModule!"
                return
            }
        }

        File srcJava = new File(api_module, "src${File.separator}main${File.separator}java")
        if (!srcJava.exists()) {
            srcJava.mkdirs()
        }

        File manifest = new File(srcJava.getParent(), "AndroidManifest.xml")
        if (!manifest.exists()) {
            manifest.createNewFile()
            File moduleManifest = new File("${path}${File.separator}src${File.separator}main${File.separator}AndroidManifest.xml")
            def packageName = "com.pixocial.alter.${ApiUtils.getApiLibraryName(moduleName).replace("-", "_")}_sdk"
            println("manifest path ----> ${moduleManifest.path}")
            if (moduleManifest.exists()) {
                def reg = ~'(?<=").*?(?=")'
                moduleManifest.eachLine { String oneLine ->
                    def matcher = oneLine =~ reg
                    if (oneLine.contains("package") && matcher.find()) {
                        def modulePackage = oneLine.split("package")[1]
                        def packageMatcher = modulePackage =~ reg
                        if (packageMatcher.find()) {
                            packageName = "${packageMatcher.group(0)}_sdk"
                            println("packageName path ----> ${packageName}")
                        }
                    }
                }
            }
            manifest.write('''<?xml version=\"1.0\" encoding=\"utf-8\"?>''')
            manifest.write("<manifest package=\"${packageName}\" />")
        }

        File buildFile = new File(api_module, "build.gradle")
        if (!buildFile.exists()) {
            buildFile.createNewFile()
            buildFile.write("plugins {\n" +
                    "    id 'com.android.library'\n" +
                    "    id 'kotlin-android'\n" +
                    "}\n" +
                    "\n" +
                    "android {\n" +
                    "    compileSdkVersion rootProject.ext.android.compileSdkVersion\n" +
                    "    buildToolsVersion rootProject.ext.android.buildToolsVersion\n" +
                    "\n" +
                    "    defaultConfig {\n" +
                    "        minSdkVersion rootProject.ext.android.minSdkVersion\n" +
                    "        targetSdkVersion rootProject.ext.android.targetSdkVersion\n" +
                    "    }\n" +
                    "}\n" +
                    "\n" +
                    "dependencies {\n" +
                    "    implementation \"org.jetbrains.kotlin:kotlin-stdlib:\$kotlin_version\"\n" +
                    "}\n")
        }

    }

    /**
     * settings include生成的api-module
     * @param settings
     * @param moduleName
     */
    private void includeApiModule(Settings settings, String moduleName) {
        String apiModuleName = ApiUtils.getApiLibraryName(moduleName)
        settings.include ":${apiModuleName}"
        settings.project(":${apiModuleName}").projectDir = new File(ApiUtils.getApiLibraryPath(settings), "${apiModuleName}")
    }

}