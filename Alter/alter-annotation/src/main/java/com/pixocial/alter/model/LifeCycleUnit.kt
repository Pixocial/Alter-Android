package com.pixocial.alter.model

import com.pixocial.alter.enums.LifeCycleProcess
import com.pixocial.alter.enums.LifeCycleThread

/**
 * 生命周期的最小单元
 */
class LifeCycleUnit : Comparable<LifeCycleUnit> {
    var destinationClassName: String? = "" //对应初始化的class
    var process: LifeCycleProcess = LifeCycleProcess.MAIN //初始化进程
    var callCreateThread: LifeCycleThread = LifeCycleThread.MAIN //初始化进程
    var priority: Int = 0 //模块内部排序，都无依赖项的前提下，数值越小越靠前
    var dependencies: MutableList<String> = ArrayList() //项目范围内的调整，在某些初始项之后初始化
    var dependenciesStr: String? = null //项目范围内的调整，在某些初始项之后初始化
    var description: String? = "" //自定义描述
    var unitName: String = ""  //当前初始化单元名称
    var moduleName: String = "" //模块名称

    constructor(destinationClassName: String?,
                processInt: Int,
                callCreateThreadInt: Int,
                priority: Int,
                dependenciesStr: String?,
                description: String?,
                unitName: String,
                moduleName: String) {
        this.destinationClassName = destinationClassName
        this.process = LifeCycleProcess.values()[processInt]
        this.callCreateThread = LifeCycleThread.values()[callCreateThreadInt]
        this.priority = priority
        this.description = description
        this.unitName = unitName
        this.moduleName = moduleName
        this.dependenciesStr = dependenciesStr
        if (!dependenciesStr.isNullOrEmpty()) {
            val split = dependenciesStr.split(",")
            split.let {
                this.dependencies.addAll(it)
            }
        }
    }

    override fun compareTo(other: LifeCycleUnit): Int {
        return if (this.priority < other.priority) -1 else if (this.priority == other.priority) 0 else 1
    }
}
