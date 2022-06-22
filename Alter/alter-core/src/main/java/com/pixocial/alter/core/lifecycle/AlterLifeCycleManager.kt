package com.pixocial.alter.core.lifecycle

import android.content.Context
import android.content.res.Configuration
import com.pixocial.alter.core.lifecycle.dispatcher.AlterLifeCycleDispatcher
import com.pixocial.alter.core.lifecycle.dispatcher.AlterLifeCycleDispatcherUnit
import com.pixocial.alter.core.lifecycle.utils.AlterLog
import com.pixocial.alter.model.LifeCycleSortStore
import com.pixocial.alter.model.LifeCycleUnit

class AlterLifeCycleManager private constructor() {

    private val mDispatcher: AlterLifeCycleDispatcher = AlterLifeCycleDispatcher()
    private var mUnitObjectsMap:MutableMap<String, AlterLifeCycleDispatcherUnit> = HashMap()
    private lateinit var mUnitSortStore: LifeCycleSortStore
    private val mUnitList: MutableList<LifeCycleUnit> = ArrayList()
    private val mUnitChildrenMap: MutableMap<String, MutableList<String>> = HashMap()

    companion object {
        @JvmStatic
        val instance = SingletonHolder.sHolder
    }

    private object SingletonHolder {
        val sHolder = AlterLifeCycleManager()
    }

    init {
        val startTime = System.currentTimeMillis()
        injectLifeCycleUnitList()
        injectUnitChildrenMap()
        AlterLog.logD("AlterLifeCycleManager", "mUnitList.size = ${mUnitList.size} mUnitChildrenMap.size = ${mUnitChildrenMap.size}")
        initUnitSortStore(LifeCycleSortStore(mUnitList, mUnitChildrenMap))
        AlterLog.logD("AlterLifeCycleManager", "init time = ${System.currentTimeMillis() - startTime}ms")
    }

    /**
     * 在插件中注入 mUnitList
     */
    private fun injectLifeCycleUnitList() {}

    /**
     * 在插件中注入 mUnitChildrenMap
     */
    private fun injectUnitChildrenMap() {}

    private fun initUnitSortStore(unitSortStore: LifeCycleSortStore) {
        mUnitSortStore = unitSortStore
        mUnitSortStore.unitList.forEach { unit ->
            mUnitObjectsMap[unit.unitName] = AlterLifeCycleDispatcherUnit(unit)
        }
    }

    fun attachBaseContext(base: Context) {
        val startTime = System.currentTimeMillis()
        mDispatcher.prepare(base)

        mUnitSortStore.unitList.forEach { unit ->
            mUnitObjectsMap[unit.unitName]?.let {
                if (!mDispatcher.isIgnoreDispatch(it)) {
                    it.attachBaseContext(base)
                }
            }
        }
        AlterLog.logD("AlterLifeCycleManager", "attachBaseContext time = ${System.currentTimeMillis() - startTime}ms")
    }

    fun onCreate(context: Context) {
        val startTime = System.currentTimeMillis()
        mUnitSortStore.unitList.forEach { unit ->
            mUnitObjectsMap[unit.unitName]?.let {
                mDispatcher.dispatch(context, it, mUnitSortStore, mUnitObjectsMap)
            }
        }
        AlterLog.logD("AlterLifeCycleManager", "onCreate time = ${System.currentTimeMillis() - startTime}ms")
    }

    fun onLowMemory() {
        mUnitSortStore.unitList.forEach { unit ->
            mUnitObjectsMap[unit.unitName]?.let {
                if (!mDispatcher.isIgnoreDispatch(it)) {
                    it.onLowMemory()
                }
            }
        }
    }

    fun onTrimMemory(level: Int) {
        mUnitSortStore.unitList.forEach { unit ->
            mUnitObjectsMap[unit.unitName]?.let {
                if (!mDispatcher.isIgnoreDispatch(it)) {
                    it.onTrimMemory(level)
                }
            }
        }
    }

    fun onConfigurationChanged(newConfig: Configuration) {
        mUnitSortStore.unitList.forEach { unit ->
            mUnitObjectsMap[unit.unitName]?.let {
                if (!mDispatcher.isIgnoreDispatch(it)) {
                    it.onConfigurationChanged(newConfig)
                }
            }
        }
    }

}