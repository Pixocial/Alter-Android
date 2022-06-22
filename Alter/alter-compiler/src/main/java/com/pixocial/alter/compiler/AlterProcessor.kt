package com.pixocial.alter.compiler

import com.pixocial.alter.annotation.AlterLifeCycle
import com.pixocial.alter.annotation.Router
import com.pixocial.alter.annotation.ServiceRegister
import com.pixocial.alter.compiler.processor.LifeCycleProcessor
import com.pixocial.alter.compiler.processor.RouterProcessor
import com.pixocial.alter.compiler.processor.ServiceProcessor
import com.pixocial.alter.compiler.router.RouteConstants
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.lang.model.util.Types
import javax.tools.Diagnostic

/**
 * Alter apt 入口类
 */
class AlterProcessor : AbstractProcessor() {

    private lateinit var mMessage: Messager
    lateinit var mFiler: Filer
    lateinit var mElementsUtils: Elements
    lateinit var mTypeUtils: Types
    var mModuleName: String? = null

    override fun init(p0: ProcessingEnvironment?) {
        super.init(p0)
        mFiler = processingEnv.filer
        mMessage = processingEnv.messager
        mElementsUtils = processingEnv.elementUtils
        mTypeUtils = processingEnv.typeUtils
        val options: Map<String, String> = processingEnv.options

        if (options.isNotEmpty()) {
            mModuleName = options[RouteConstants.ARGUMENTS_NAME]
        }
        if (mModuleName.isNullOrEmpty()) {
            throw RuntimeException("AlterProcessor not set processor moduleName option !")
        }
        mModuleName = mModuleName!!.replace("-", "_")
        //i("init")
    }

    override fun process(p0: MutableSet<out TypeElement>?, p1: RoundEnvironment?): Boolean {
        if (p0 == null || p0.isEmpty()) {
            return false
        }
        i("process")

        ServiceProcessor().process(p1, processingEnv, this, mModuleName!!)
        RouterProcessor().process(p1, processingEnv, this, mModuleName!!)
        LifeCycleProcessor().process(p1, processingEnv, this, mModuleName!!)

        return true
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        i("getSupportedAnnotationTypes")
        return mutableSetOf(
                ServiceRegister::class.java.canonicalName,
                Router::class.java.canonicalName,
                AlterLifeCycle::class.java.canonicalName
        )
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    override fun getSupportedOptions(): MutableSet<String> {
        val supportedOptions = HashSet<String>()
        supportedOptions.add(RouteConstants.ARGUMENTS_NAME)
        return supportedOptions
    }

    fun i(msg: String) {
        mMessage.printMessage(Diagnostic.Kind.NOTE, "--> Alter AlterProcessor $msg ")
    }
}