package com.pixocial.alter.plugins.router

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import java.io.File
import java.util.jar.JarFile

class AlterRouteTransform : Transform() {
    var mInitClassFile: File? = null

    companion object {
        val REGISTER_CLASS_FILE_NAME = "com/pixocial/alter/core/router/utils/AlterRouteCenter.class"
    }

    override fun getName(): String {
        return "AlterRouteTransform"
    }

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> {
        return TransformManager.CONTENT_CLASS
    }

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    override fun isIncremental(): Boolean {
        return true
    }

    override fun transform(context: Context?, inputs: MutableCollection<TransformInput>?, referencedInputs: MutableCollection<TransformInput>, outputProvider: TransformOutputProvider, isIncremental: Boolean) {

        println("--> Alter AlterRouteTransform transform start")

        val routeRootList: MutableList<String> = ArrayList()

        inputs?.forEach { input ->
            input.directoryInputs.forEach { directoryInput ->
                if (directoryInput.file.isDirectory) {
                    directoryInput.file.walk().filter { it.isFile }.forEach { file ->
                        //println("--> Alter AlterRouteTransform transform directoryInput ${file.name}")
                        if (file.name.contains("AlterRouter_Root_")) {
                            val className = file.path.replace(directoryInput.file.path + "/", "")
                            routeRootList.add(className)
                        }
                    }
                }
                val dest = outputProvider.getContentLocation(directoryInput.name, directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY)
                FileUtils.copyDirectory(directoryInput.file, dest)
            }

            input.jarInputs.forEach { jarInput ->
                var jarName = jarInput.name
                val md5 = DigestUtils.md5Hex (jarInput.file.absolutePath)
                if (jarName.endsWith(".jar")) {
                    jarName = jarName.substring(0, jarName.length - 4)
                }
                val dest = outputProvider.getContentLocation (jarName + md5, jarInput.contentTypes, jarInput.scopes, Format.JAR)
                if (jarInput.file.absolutePath.endsWith(".jar")) {
                    //处理jar包里的代码
                    val src = jarInput.file
                    if (shouldProcessPreDexJar(src.absolutePath)) {
                        val list = scanJar (src, dest)
                        if (list.isNotEmpty()) {
                            routeRootList.addAll(list)
                        }
                    }
                }
                FileUtils.copyFile(jarInput.file, dest)
            }
        }

        routeRootList.forEach {
            println("--> Alter AlterRouteTransform transform $it")
        }
        println("--> Alter AlterRouteTransform transform manager jar ${mInitClassFile?.name}")
        AlterRouteCodeInjector(mInitClassFile, routeRootList).execute()
    }

    private fun shouldProcessPreDexJar(path: String): Boolean {
        return !path.contains("com.android.support") && !path.contains("/android/m2repository")
    }

    private fun scanJar(jarFile: File, destFile: File): List<String> {
        val file = JarFile(jarFile)
        val enumeration = file.entries()
        val list: MutableList<String> = ArrayList()
        while (enumeration.hasMoreElements()) {
            val jarEntry = enumeration.nextElement()
            val entryName = jarEntry.name
            //println("--> Alter AlterRouteTransform transform scanJar ${entryName}")
            if (entryName == REGISTER_CLASS_FILE_NAME) {
                mInitClassFile = destFile
            } else {
                if (entryName.contains("AlterRouter_Root_")) {
                    list.add(entryName)
                }
            }
        }
        return list
    }
}