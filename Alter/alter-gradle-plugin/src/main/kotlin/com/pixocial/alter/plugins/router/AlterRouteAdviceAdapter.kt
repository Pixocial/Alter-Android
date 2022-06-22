package com.pixocial.alter.plugins.router

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.AdviceAdapter

class AlterRouteAdviceAdapter(routeRootList: List<String>, mv: MethodVisitor, access: Int, name: String?, desc: String?) : AdviceAdapter(Opcodes.ASM5, mv, access, name, desc) {

    private val mRouteRootList = routeRootList

    override fun onMethodEnter() {
        super.onMethodEnter()
        println("--> Alter AlterRouteTransform transform onMethodEnter")

        mRouteRootList.forEach { name ->
            val fullName = name.substring(0, name.length - 6).replace("/", ".")
            mv.visitVarInsn(ALOAD, 0)
            mv.visitLdcInsn(fullName)
            mv.visitMethodInsn(INVOKEVIRTUAL, "com/pixocial/alter/core/router/utils/AlterRouteCenter", "registerRoute", "(Ljava/lang/String;)V", false)
            println("--> Alter AlterRouteTransform transform onMethodEnter fullName:$fullName")
        }
    }

}