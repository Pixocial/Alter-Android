package com.pixocial.alter.compiler.processor

import com.pixocial.alter.compiler.AlterProcessor
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.TypeName
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment

abstract class BaseProcessor {
    protected lateinit var mProcessor: AlterProcessor
    protected var mRoundEnv: RoundEnvironment? = null
    protected lateinit var mProcessingEnv: ProcessingEnvironment
    protected lateinit var mModuleName: String

    open fun process(
        roundEnv: RoundEnvironment?,
        processingEnv: ProcessingEnvironment,
        mAbstractProcessor: AlterProcessor,
        moduleName: String
    ) {
        mProcessor = mAbstractProcessor
        mRoundEnv = roundEnv
        mProcessingEnv = processingEnv
        mModuleName = moduleName
    }

    protected open fun getTypeName(canonicalName: String?): TypeName? {
        return ClassName.get(mProcessingEnv.elementUtils.getTypeElement(canonicalName))
    }

    protected open fun getOption(key: String?): String? {
        return mProcessingEnv.options[key]
    }
}