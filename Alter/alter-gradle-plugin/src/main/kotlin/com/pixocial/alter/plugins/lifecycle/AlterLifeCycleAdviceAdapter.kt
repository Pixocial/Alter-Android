package com.pixocial.alter.plugins.lifecycle

import com.pixocial.alter.model.LifeCycleSortStore
import com.pixocial.alter.plugins.utils.INJECT_METHOD_UNIT_CHILDREN_MAP
import com.pixocial.alter.plugins.utils.INJECT_METHOD_UNIT_LIST
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.AdviceAdapter

class AlterLifeCycleAdviceAdapter(sortInitStore: LifeCycleSortStore, mv: MethodVisitor, access: Int, name: String?, desc: String?) : AdviceAdapter(Opcodes.ASM5, mv, access, name, desc) {

    private val mLifeCycleSortInitStore = sortInitStore
    private val mMethodName = name

    override fun onMethodEnter() {
        super.onMethodEnter()
        //println("--> Alter AlterLifeCyclePlugin transform onMethodEnter")
        if (INJECT_METHOD_UNIT_LIST == mMethodName) {
            injectUnitList()
        } else if (INJECT_METHOD_UNIT_CHILDREN_MAP == mMethodName) {
            injectUnitChildrenMap()
        }
    }

    private fun injectUnitChildrenMap() {
        println("--> Alter AlterLifeCycleTransform transform injectUnitChildrenMap")
        mLifeCycleSortInitStore.unitChildrenMap.forEach { (parentUnitName, childUnitList) ->
            mv.visitTypeInsn(Opcodes.NEW, "java/util/ArrayList")
            mv.visitInsn(Opcodes.DUP)
            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/util/ArrayList", "<init>", "()V", false)
            mv.visitTypeInsn(Opcodes.CHECKCAST, "java/util/List")
            mv.visitVarInsn(Opcodes.ASTORE, 1)

            childUnitList.forEach { childUnitName ->
                mv.visitVarInsn(Opcodes.ALOAD, 1)
                mv.visitLdcInsn(childUnitName)
                mv.visitMethodInsn(Opcodes.INVOKEINTERFACE, "java/util/List", "add", "(Ljava/lang/Object;)Z", true)
                mv.visitInsn(Opcodes.POP)
            }

            mv.visitVarInsn(Opcodes.ALOAD, 0)
            mv.visitFieldInsn(Opcodes.GETFIELD, "com/pixocial/alter/core/lifecycle/AlterLifeCycleManager", "mUnitChildrenMap", "Ljava/util/Map;")
            mv.visitLdcInsn(parentUnitName)
            mv.visitVarInsn(Opcodes.ALOAD, 1)
            mv.visitMethodInsn(Opcodes.INVOKEINTERFACE, "java/util/Map", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", true)
            mv.visitInsn(Opcodes.POP)
        }

    }

    private fun injectUnitList() {
        println("--> Alter AlterLifeCycleTransform transform injectUnitList")
        mLifeCycleSortInitStore.unitList.forEach { unit ->
            mv.visitVarInsn(Opcodes.ALOAD, 0)
            mv.visitFieldInsn(Opcodes.GETFIELD, "com/pixocial/alter/core/lifecycle/AlterLifeCycleManager", "mUnitList", "Ljava/util/List;")
            mv.visitTypeInsn(Opcodes.NEW, "com/pixocial/alter/model/LifeCycleUnit")
            mv.visitInsn(Opcodes.DUP)
            mv.visitLdcInsn(unit.destinationClassName)
            mv.visitIntInsn(Opcodes.SIPUSH, unit.process.ordinal)
            mv.visitIntInsn(Opcodes.SIPUSH, unit.callCreateThread.ordinal)
            mv.visitIntInsn(Opcodes.SIPUSH, unit.priority)
            mv.visitLdcInsn(unit.dependenciesStr)
            mv.visitLdcInsn(unit.description)
            mv.visitLdcInsn(unit.unitName)
            mv.visitLdcInsn(unit.moduleName)
            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "com/pixocial/alter/model/LifeCycleUnit", "<init>",
                    "(Ljava/lang/String;IIILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V",
                    false)
            mv.visitMethodInsn(Opcodes.INVOKEINTERFACE, "java/util/List", "add", "(Ljava/lang/Object;)Z", true)
            mv.visitInsn(Opcodes.POP)
        }
    }

}