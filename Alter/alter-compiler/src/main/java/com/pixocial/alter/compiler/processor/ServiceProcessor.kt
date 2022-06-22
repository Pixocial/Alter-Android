package com.pixocial.alter.compiler.processor

import com.pixocial.alter.annotation.ServiceRegister
import com.pixocial.alter.compiler.AlterProcessor
import com.squareup.javapoet.*
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.type.MirroredTypeException
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.ElementFilter

/**
 * Alter 接口服务处理类
 */
class ServiceProcessor : BaseProcessor() {

    private val provider: ClassName = ClassName.get("com.pixocial.alter.core.service", "IAlterServiceProvider")

    override fun process(roundEnv: RoundEnvironment?, processingEnv: ProcessingEnvironment, mAbstractProcessor: AlterProcessor, moduleName: String) {
        super.process(roundEnv, processingEnv, mAbstractProcessor, moduleName)
        genProxyClass(roundEnv)
    }

    /**
     * 生成实现了com.pixocial.alter.core.AlterProvider接口的代理类
     */
    private fun genProxyClass(roundEnv: RoundEnvironment?) {
        ElementFilter.typesIn(roundEnv?.getElementsAnnotatedWith(ServiceRegister::class.java)).forEach {
            val qualifiedName = getAnnotationClassValue(it.getAnnotation(ServiceRegister::class.java))!!.toString()
            val className = qualifiedName.substring(qualifiedName.lastIndexOf(".") + 1) + "\$\$AlterBinder"
            val packageName = qualifiedName.substring(0, qualifiedName.lastIndexOf("."))

            val presenterClass = ClassName.bestGuess(qualifiedName)

            //i("qualifiedName:$qualifiedName className:$className packageName:$packageName presenterClass:$presenterClass")
            TypeSpec.classBuilder(className)
                    .addJavadoc("Alter代理类，由apt自动生成")
                    .addModifiers(Modifier.FINAL, Modifier.PUBLIC)
                    .addSuperinterface(ParameterizedTypeName.get(provider, presenterClass))
                    .let { builder ->
                        genOverrideMethod(builder, it, presenterClass)
                        try {
                            val javaFile = JavaFile.builder(packageName, builder.build()).build()
                            javaFile.writeTo(mProcessor.mFiler)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
        }
    }

    /**
     * 获取注解ServiceRegister中serviceInterface的class类型
     */
    private fun getAnnotationClassValue(annotation: ServiceRegister): TypeMirror? {
        try {
            annotation.serviceInterface
        } catch (mte: MirroredTypeException) {
            if (mte.typeMirror != null) {
                return mte.typeMirror
            }
        }
        return null
    }

    /**
     * 生成实现AlterProvider中的buildAlterService重载方法
     */
    private fun genOverrideMethod(typeBuilder: TypeSpec.Builder, typeElement: TypeElement, serviceClass: ClassName) {
        MethodSpec.methodBuilder("buildAlterService")
                .addAnnotation(Override::class.java)
                .addParameter(ParameterizedTypeName.get(ClassName.get(Class::class.java), serviceClass), "clazz")
                .addModifiers(Modifier.PUBLIC)
                .returns(serviceClass)
                .addStatement("return new \$T()", ClassName.get(typeElement))
                .let { typeBuilder.addMethod(it.build()) }
    }
}