package com.pixocial.alter.plugins.lifecycle

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.pixocial.alter.base.AlterLifeCycleBaseModuleTable
import com.pixocial.alter.plugins.utils.sortInitModule
import groovy.lang.GroovyClassLoader
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.gradle.api.Project
import java.io.File
import java.util.jar.JarFile

class AlterLifeCycleTransform(project: Project) : Transform() {

    private var mProject: Project = project

    private var mInitClassFile: File? = null

    private lateinit var mUrlClassLoader: GroovyClassLoader

    companion object {
        val REGISTER_CLASS_FILE_NAME = "com/pixocial/alter/core/lifecycle/AlterLifeCycleManager.class"
    }

    override fun getName(): String {
        return "AlterLifeCycleTransform"
    }

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> {
        return TransformManager.CONTENT_CLASS
    }

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    override fun isIncremental(): Boolean {
        return false
    }

    override fun transform(context: Context?, inputs: MutableCollection<TransformInput>?, referencedInputs: MutableCollection<TransformInput>, outputProvider: TransformOutputProvider, isIncremental: Boolean) {

        println("--> Alter AlterLifeCycleTransform transform start")

        mUrlClassLoader = GroovyClassLoader()

        val initModuleClassNameList: MutableSet<String> = HashSet()
        val initModuleObjectList: MutableList<AlterLifeCycleBaseModuleTable> = ArrayList()

        inputs?.forEach { input ->
            input.directoryInputs.forEach { directoryInput ->
                mUrlClassLoader.addClasspath(directoryInput.file.absolutePath)
                if (directoryInput.file.isDirectory) {
                    directoryInput.file.walk().filter { it.isFile }.forEach { file ->
                        //println("--> Alter AlterLifeCycleTransform transform directoryInput ${file.name}")
                        if (file.name.contains("\$\$AlterLifeCycleBinder")) {
                            val className = file.path.replace(directoryInput.file.path + "/", "")
                            initModuleClassNameList.add(className)
                        }
                    }
                }
                val dest = outputProvider.getContentLocation(directoryInput.name, directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY)
                FileUtils.copyDirectory(directoryInput.file, dest)
            }

            input.jarInputs.forEach { jarInput ->
                mUrlClassLoader.addClasspath(jarInput.file.absolutePath)
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
                                    initModuleClassNameList.addAll(list)
                                }
                            }
                }
                FileUtils.copyFile(jarInput.file, dest)
            }
        }

        initModuleClassNameList.forEach {
            //println("--> Alter AlterLifeCycleTransform transform $it")
            val className = it.replace("/", ".").replace(".class", "")
            initModuleObjectList.add(loadClass(className).newInstance() as AlterLifeCycleBaseModuleTable)
        }

        println("--> Alter AlterLifeCycleTransform transform manager jar ${mInitClassFile?.name}")

        println("--> Alter AlterLifeCycleTransform transform initUnitList ${initModuleObjectList.size}")

        val sortInitStore = sortInitModule(initModuleObjectList)

        AlterLifeCycleCodeInjector(mInitClassFile, sortInitStore).execute()

        mUrlClassLoader.clearCache()
        mUrlClassLoader.close()
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
            //println("--> Alter AlterLifeCycleTransform transform scanJar ${entryName}")
            if (entryName == REGISTER_CLASS_FILE_NAME) {
                mInitClassFile = destFile
            } else {
                if (entryName.contains("\$\$AlterLifeCycleBinder")) {
                    list.add(entryName)
                }
            }
        }
        return list
    }

    private fun loadClass(name: String): Class<*> {
        println("--> Alter AlterLifeCycleTransform loadClass $name")
        return mUrlClassLoader.loadClass(name)
    }
}