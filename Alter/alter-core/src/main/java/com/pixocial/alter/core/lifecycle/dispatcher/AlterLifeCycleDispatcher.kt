package com.pixocial.alter.core.lifecycle.dispatcher

import android.content.Context
import com.pixocial.alter.core.lifecycle.utils.AlterLog
import com.pixocial.alter.core.lifecycle.utils.AlterUtils
import com.pixocial.alter.model.LifeCycleSortStore

class AlterLifeCycleDispatcher : IManagerDispatcher {

    private val TAG = "AlterLifeCycleDispatcher"

    private var mIsMainProcess = true
    private var mInitializedUnitSet: MutableSet<String> = HashSet()

    override fun prepare(context: Context) {
        mIsMainProcess = AlterUtils.isMainProcess(context)
    }

    override fun dispatch(context: Context, initUnit: AlterLifeCycleDispatcherUnit, sortStore: LifeCycleSortStore, unitObjectsMap: Map<String, AlterLifeCycleDispatcherUnit>) {
        if (isIgnoreDispatch(initUnit)) {
            AlterLog.logD(TAG, "ignore init ${initUnit.unitName}")
            return
        }
        if (mInitializedUnitSet.contains(initUnit.unitName)) {
            AlterLog.logD(TAG, "${initUnit.unitName} had initialized!!")
            notifyChildren(initUnit, sortStore, unitObjectsMap)
        } else {
            val runnable = AlterLifeCycleUnitCreateRunnable(context, initUnit, sortStore, unitObjectsMap, this)
            if (!initUnit.callCreateOnMainThread()) {
                initUnit.createExecutor().execute(runnable)
            } else {
                runnable.run()
            }
        }
    }

    override fun notifyChildren(unitParent: AlterLifeCycleDispatcherUnit, sortStore: LifeCycleSortStore, unitObjectsMap: Map<String, AlterLifeCycleDispatcherUnit>) {
        sortStore.unitChildrenMap[unitParent.unitName]?.forEach { childUnitName ->
            val childUnitObject = unitObjectsMap[childUnitName]?.lifeCycleObject
            childUnitObject?.onDependenciesCompleted(unitParent.unitName)
            unitObjectsMap[childUnitName]?.toNotify()
        }
    }

    override fun saveInitializedUnit(unitName: String) {
        mInitializedUnitSet.add(unitName)
    }

    /**
     * 是否忽略该初始化
     */
    fun isIgnoreDispatch(initUnit: AlterLifeCycleDispatcherUnit): Boolean {
        val dispatch = mIsMainProcess && initUnit.isForMainProcess()
                || !mIsMainProcess && initUnit.isNotForMainProcess()
        return !dispatch
    }
}