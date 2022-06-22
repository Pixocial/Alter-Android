package com.pixocial.alter.core.lifecycle.dispatcher

import android.content.Context
import android.content.res.Configuration
import com.pixocial.alter.core.lifecycle.IAlterLifeCycle
import com.pixocial.alter.core.lifecycle.executor.AlterExecutorManager
import com.pixocial.alter.core.lifecycle.executor.IExecutor
import com.pixocial.alter.core.lifecycle.utils.AlterLog
import com.pixocial.alter.enums.LifeCycleProcess
import com.pixocial.alter.enums.LifeCycleThread
import com.pixocial.alter.model.LifeCycleUnit
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executor

/**
 * LifeCycle 初始化线程调度单位
 */
class AlterLifeCycleDispatcherUnit(unit: LifeCycleUnit) : IAlterLifeCycle, IDispatcher, IExecutor {

    private val TAG = "AlterLifeCycleDispatcherUnit"

    var lifeCycleObject: IAlterLifeCycle? = null
    var process: LifeCycleProcess = LifeCycleProcess.MAIN //初始化进程
    var callCreateThread: LifeCycleThread = LifeCycleThread.MAIN //初始化进程
    var priority: Int = 0 //模块内部排序，都无依赖项的前提下，数值越小越靠前
    var dependencies: MutableList<String> = ArrayList() //项目范围内的调整，在某些初始项之后初始化
    var description: String? = "" //自定义描述
    var unitName: String = ""  //当前初始化单元名称
    var moduleName: String = "" //模块名称
    var startWaitTime: Long = 0L

    private var mWaitCountDown: CountDownLatch

    init {
        this.process = unit.process
        this.callCreateThread = unit.callCreateThread
        this.priority = unit.priority
        this.description = unit.description
        this.unitName = unit.unitName
        this.moduleName = unit.moduleName
        this.dependencies = unit.dependencies
        this.mWaitCountDown = CountDownLatch(this.dependencies.size)
        try {
            unit.destinationClassName?.let {
                val newInstance = Class.forName(it).getConstructor().newInstance()
                if (newInstance is IAlterLifeCycle) {
                    this.lifeCycleObject = newInstance
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            AlterLog.logW(TAG, "new instance for ${unit.destinationClassName} has exception!!")
        }
    }

    override fun callCreateOnMainThread(): Boolean {
        return callCreateThread == LifeCycleThread.MAIN
    }

    override fun onDependenciesCompleted(unitName: String) {
        lifeCycleObject?.onDependenciesCompleted(unitName)
    }

    override fun toWait() {
        try {
            startWaitTime = System.currentTimeMillis()
            AlterLog.logD(TAG, "$unitName to wait!! curTime=${startWaitTime} ${dependencies.size}")
            mWaitCountDown.await()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    override fun toNotify() {
        AlterLog.logD(TAG, "$unitName to notify!!")
        mWaitCountDown.countDown()
    }

    override fun createExecutor(): Executor = AlterExecutorManager.instance.ioExecutor

    override fun attachBaseContext(base: Context?) {
        lifeCycleObject?.attachBaseContext(base)
    }

    override fun onCreate(context: Context) {
        val startTime = System.currentTimeMillis()
        lifeCycleObject?.onCreate(context)
        val endTime = System.currentTimeMillis()
        AlterLog.logD(TAG, "$unitName onCreate completed!! from wait to complete time=${System.currentTimeMillis() - startWaitTime}ms, onreate time=${endTime-startTime}ms")
    }

    override fun onLowMemory() {
        lifeCycleObject?.onLowMemory()
    }

    override fun onTrimMemory(level: Int) {
        lifeCycleObject?.onTrimMemory(level)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        lifeCycleObject?.onConfigurationChanged(newConfig)
    }

    fun isForMainProcess(): Boolean {
        return process == LifeCycleProcess.MAIN || isForAllProcess()
    }

    fun isForAllProcess(): Boolean {
        return process == LifeCycleProcess.ALL
    }

    fun isNotForMainProcess(): Boolean {
        return process != LifeCycleProcess.MAIN && !isForAllProcess()
    }
}