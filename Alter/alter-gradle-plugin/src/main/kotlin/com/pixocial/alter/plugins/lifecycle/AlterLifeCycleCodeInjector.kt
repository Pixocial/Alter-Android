package com.pixocial.alter.plugins.lifecycle

import com.pixocial.alter.model.LifeCycleSortStore
import org.apache.commons.io.IOUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import java.io.File
import java.io.FileOutputStream
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

class AlterLifeCycleCodeInjector(managerJarFile: File?, sortInitStore: LifeCycleSortStore) {

    private var mInitClassFile: File? = managerJarFile
    private val mLifeCycleSortInitStore = sortInitStore

    fun execute() {
        if (mInitClassFile != null && mLifeCycleSortInitStore.unitList.isNotEmpty()) {
            val srcFile = mInitClassFile!!
            val optJar = File(srcFile.parent, srcFile.name + ".opt")
            if (optJar.exists()) {
                optJar.delete()
            }
            val file = JarFile(srcFile)
            val enumeration = file.entries()
            val jarOutputStream = JarOutputStream(FileOutputStream(optJar))
            while (enumeration.hasMoreElements()) {
                val jarEntry = enumeration.nextElement()
                val entryName = jarEntry.name
                val zipEntry = ZipEntry(entryName)
                val inputStream = file.getInputStream(jarEntry)
                jarOutputStream.putNextEntry(zipEntry)

                //找到需要插入代码的class，通过ASM动态注入字节码
                if (AlterLifeCycleTransform.REGISTER_CLASS_FILE_NAME == entryName) {
                    val classReader = ClassReader(inputStream)
                    // 构建一个ClassWriter对象，并设置让系统自动计算栈和本地变量大小
                    val classWriter = ClassWriter(ClassWriter.COMPUTE_MAXS)
                    val classVisitor = AlterLifeCycleClassVisitor(mLifeCycleSortInitStore, classWriter)
                    //开始扫描class文件
                    classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)

                    val bytes = classWriter.toByteArray()
                    //将注入过字节码的class，写入临时jar文件里
                    jarOutputStream.write(bytes)
                } else {
                    //不需要修改的class，原样写入临时jar文件里
                    jarOutputStream.write(IOUtils.toByteArray(inputStream))
                }
                inputStream.close()
                jarOutputStream.closeEntry()
            }

            jarOutputStream.close()
            file.close()

            //删除原来的jar文件
            if (srcFile.exists()) {
                srcFile.delete()
            }
            //重新命名临时jar文件，新的jar包里已经包含了我们注入的字节码了
            optJar.renameTo(srcFile)
        }
    }

}