package com.pixocial.alter.annotation

import com.pixocial.alter.enums.LifeCycleProcess
import com.pixocial.alter.enums.LifeCycleThread

/**
 * Alter 框架中，用于模块 IAlterLifeCycle 实现类的注解
 *
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class AlterLifeCycle (
    val process: LifeCycleProcess = LifeCycleProcess.MAIN, //定义在哪个进程初始化，默认主进程
    val callCreateThread: LifeCycleThread = LifeCycleThread.MAIN, //定义在哪个线程初始化
    val priority: Int = 0, //模块内部排序，都无依赖项的前提下，数值越小越靠前
    val dependencies: String = "", //项目范围内的调整，在某些初始项之后初始化，多个用 ',' 分隔
    val description: String = "" //自定义描述
)