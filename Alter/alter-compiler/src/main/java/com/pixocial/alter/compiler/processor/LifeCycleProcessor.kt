package com.pixocial.alter.compiler.processor

import com.pixocial.alter.annotation.AlterLifeCycle
import com.pixocial.alter.compiler.AlterProcessor
import com.pixocial.alter.compiler.router.RouteConstants.Companion.PACKAGE_OF_LIFECYCLE_FILE
import com.pixocial.alter.enums.LifeCycleProcess
import com.pixocial.alter.enums.LifeCycleThread
import com.pixocial.alter.model.LifeCycleUnit
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.Modifier
import javax.lang.model.element.Name
import javax.lang.model.util.ElementFilter

class LifeCycleProcessor : BaseProcessor() {

    override fun process(roundEnv: RoundEnvironment?, processingEnv: ProcessingEnvironment, mAbstractProcessor: AlterProcessor, moduleName: String) {
        super.process(roundEnv, processingEnv, mAbstractProcessor, moduleName)
        mProcessor = mAbstractProcessor
        processLifeCycle(roundEnv)
    }

    private fun processLifeCycle(roundEnv: RoundEnvironment?) {
        //被 AlterLifeCycle 注解的节点集合
        var classBuilder: TypeSpec.Builder? = null
        var constructMethodBuilder: MethodSpec.Builder? = null
        var newClassName = ""
        ElementFilter.typesIn(roundEnv?.getElementsAnnotatedWith(AlterLifeCycle::class.java)).forEach {
            if (!it.kind.isClass) {
                throw RuntimeException("AlterLifeCycle can only be used in class.")
            }
            if (it.interfaces.isEmpty()) {
                throw RuntimeException(it.qualifiedName.toString() + " must implements interface com.pixocial.alter.core.lifecycle.IAlterLifeCycle")
            }
            var checkInterfaceFlag = false
            it.interfaces.forEach { mirror ->
                if ("com.pixocial.alter.core.lifecycle.IAlterLifeCycle".equals(mirror.toString())) {
                    checkInterfaceFlag = true
                }
            }
            if (!checkInterfaceFlag) {
                throw RuntimeException(it.qualifiedName.toString() + " must implements interface com.pixocial.alter.core.lifecycle.IAlterLifeCycle")
            }

            if (classBuilder == null) {
                val className = "$mModuleName\$\$AlterLifeCycleBinder"

                newClassName = "$PACKAGE_OF_LIFECYCLE_FILE.$className"

                classBuilder = TypeSpec.classBuilder(className)
                classBuilder!!.superclass(getTypeName("com.pixocial.alter.base.AlterLifeCycleBaseModuleTable"))
                        .addJavadoc("AlterLifeCycle代理类，由apt自动生成，不要修改")
                        .addModifiers(Modifier.FINAL, Modifier.PUBLIC)
                constructMethodBuilder = genConstructMethod()
            }

            val lifeCycle = it.getAnnotation(AlterLifeCycle::class.java)
            mProcessor.i("=====> processLifeCycle description=${lifeCycle.description} newClassName=$newClassName")

            addLifeCycleUnit(it.qualifiedName.toString(), constructMethodBuilder!!, lifeCycle, it.simpleName)
        }

        try {
            if (classBuilder != null) {
                classBuilder?.addMethod(constructMethodBuilder?.build())
                val javaFile = JavaFile.builder(PACKAGE_OF_LIFECYCLE_FILE, classBuilder!!.build()).build()
                javaFile.writeTo(mProcessor.mFiler)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun genConstructMethod(): MethodSpec.Builder? {
        val constructorMethod = MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC)
        constructorMethod.addModifiers(Modifier.PUBLIC)
                .addStatement("setModuleName(\$S)", mModuleName)
        return constructorMethod
    }

    private fun addLifeCycleUnit(className: String, methodBuilder: MethodSpec.Builder, lifeCycle: AlterLifeCycle, simpleName: Name) {
        methodBuilder.addStatement("addLifeCycleUnit(new \$T(\$S, \$L, \$L, \$L, \$S, \$S, \$S, \$S))",
                ClassName.get(LifeCycleUnit::class.java),
                className,
                lifeCycle.process.ordinal,
                lifeCycle.callCreateThread.ordinal,
                lifeCycle.priority,
                lifeCycle.dependencies,
                lifeCycle.description,
                "$mModuleName:$simpleName",
                mModuleName)
    }

}