package com.pixocial.alter.plugins.router

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class AlterRouteClassVisitor(routeRootList: List<String>, classVisitor: ClassVisitor) : ClassVisitor(Opcodes.ASM5, classVisitor) {

    private val mRouteRootList = routeRootList

    override fun visitMethod(access: Int, name: String?, desc: String?, signature: String?, exceptions: Array<out String>?): MethodVisitor {
        var mv = super.visitMethod(access, name, desc, signature, exceptions)
        //println("--> Alter AlterRouteTransform transform visitMethod $name")
        if ("loadRouteInfo" == name) {
            mv = AlterRouteAdviceAdapter(mRouteRootList, mv, access, name, desc)
        }
        return mv
    }

}