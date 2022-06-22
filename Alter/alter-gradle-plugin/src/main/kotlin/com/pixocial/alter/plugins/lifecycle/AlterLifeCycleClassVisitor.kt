package com.pixocial.alter.plugins.lifecycle

import com.pixocial.alter.model.LifeCycleSortStore
import com.pixocial.alter.plugins.utils.INJECT_METHOD_UNIT_CHILDREN_MAP
import com.pixocial.alter.plugins.utils.INJECT_METHOD_UNIT_LIST
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class AlterLifeCycleClassVisitor(sortInitStore: LifeCycleSortStore, classVisitor: ClassVisitor) : ClassVisitor(Opcodes.ASM5, classVisitor) {

    private val mLifeCycleSortInitStore = sortInitStore

    override fun visitMethod(access: Int, name: String?, desc: String?, signature: String?, exceptions: Array<out String>?): MethodVisitor {
        var mv = super.visitMethod(access, name, desc, signature, exceptions)
        if (INJECT_METHOD_UNIT_LIST == name || INJECT_METHOD_UNIT_CHILDREN_MAP == name) {
            mv = AlterLifeCycleAdviceAdapter(mLifeCycleSortInitStore, mv, access, name, desc)
        }
        return mv
    }

}